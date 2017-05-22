package com.redbubble.takehome.price

import com.redbubble.takehome.data.structure.PriceEntry
import com.redbubble.takehome.exception.{InvalidOptionsException, InvalidProductTypeException, PricesCollisionException}
import com.redbubble.takehome.util.{LazyLogging, ScalaMapper}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Holds pricing data per product with relevant options.
  *
  * @see [[com.redbubble.takehome.calculator.Calculator]]
  * @param priceData PriceEntries that will make up this tree.
  * @author vdonets
  */
class PriceTree
(priceData: Iterable[PriceEntry]) extends LazyLogging {

  private val roots = {
    val roots = new mutable.HashMap[String, TreeRoot]()
    priceData.foreach(entry => {
      logger.debug("entry = " + entry)
      val treeRoot = roots.getOrElse(entry.prodType, {
        logger.trace("No options for [" + entry.prodType + "]")
        val opts = entry.options.keySet.toSeq

        val r = TreeRoot(
          TreeNode(
            if (opts.nonEmpty)
              opts.head
            else
              ""), entry.options.keySet.toSeq)
        roots.put(entry.prodType, r)
        r
      })
      logger.debug("options = " + treeRoot.options)

      val optionStack = mutable.Stack(treeRoot.options: _*)
      var level: Iterable[Node] = Iterable(treeRoot.root)
      for (i <- optionStack.indices) {
        val optionName = optionStack.pop
        logger.trace("setting option [" + optionName + "]")
        val nextLevel = new ArrayBuffer[Node]
        entry.options(optionName).foreach(optValue => {
          logger.trace("option value = " + optValue)
          level.foreach({
            case node: TreeNode =>
              if (optionStack.nonEmpty)
                nextLevel += node.addNode(optValue, TreeNode(optionStack.top))
              else {
                val next = node.leafs.get(optValue)
                if (next.isDefined && next.get.isInstanceOf[LeafNode])
                  throw new PricesCollisionException("Found price collision for product [" + entry.prodType +
                    "] with options " + entry.options + " and price [" + entry.price + "]")
                node.addNode(optValue, LeafNode(entry.price))
              }

            case default => throw new IllegalStateException("Invalid node " + default)
          })
        })
        level = nextLevel

      }
    })
    logger.info("roots = " + ScalaMapper.default_instance.writeValueAsString(roots))
    roots.toMap
  }

  /**
    * Looks up price for a product with given options
    *
    * @param prodType type of product
    * @param options  options for this product to look up. Option keys
    *                 will be matched against valid option keys/names
    *                 for given product type. All required options must
    *                 be provided. Extras will be ignored.
    * @return price
    */
  @throws[InvalidProductTypeException]
  @throws[InvalidOptionsException]
  def getPrice(prodType: String, options: Map[String, String]): Double = {
    val root = roots.getOrElse(prodType,
      throw new InvalidProductTypeException("Invalid product type [" + prodType + "]"))
    logger.debug("Looking up price for [" + prodType + "] with options " + options)
    var node: Node = root.root
    root.options.foreach(optionKey => {
      val optVal = options.getOrElse(optionKey, throw new InvalidOptionsException("Missing option [" + optionKey + "]"))
      logger.trace("visiting [" + node + "] with optVal [" + optVal + "]")
      node = {
        val n = node.get(optVal)
        n match {
          case nextNode: LeafNode =>
            logger.trace("Found price [" + nextNode.price + "]")
            return nextNode.price
          case _ => n
        }
      }
    })
    throw new IllegalStateException("Unable to find product [" + prodType + "] with options " + options)
  }

}

/**
  * A node in a tree that has nodes below it. Represents one option
  * in a cart for a product
  *
  * @param optionName name of option this node is for. For pretty logging
  * @param leafs      other nodes under this node
  */
private[price] case class TreeNode
(private[price] val optionName: String,
 private[price] val leafs: mutable.HashMap[String, Node] = new mutable.HashMap[String, Node]())
  extends Node with LazyLogging {

  private[price] def addNode(optValue: String, next: Node): Node = {
    logger.debug("Adding node " + next + " as [" + optValue + "] to [" + optionName + "]")
    leafs.getOrElse(optValue, {
      logger.debug("Created node " + next)
      leafs.put(optValue, next)
      next
    })

  }

  override private[price] def get(optionKey: String) =
    leafs.getOrElse(optionKey, throw new InvalidOptionsException("Invalid option [" + optionKey + "]"))

  override def toString(): String = {
    return "TreeNode[" + optionName + " = " + leafs.keys + "]"
  }
}

/**
  * Holds price instead of other nodes under it
  *
  * @param price price associated with a product
  *              with options that lead up to this leaf
  */
private[price] case class LeafNode(price: Double) extends Node {
  override private[price] def get(optionKey: String) = null
}

/**
  * Node abstraction that a price tree is made up of.
  */
private trait Node {
  private[price] def get(optionKey: String): Node
}

/**
  * The root of a price tree. A price tree will be built for
  * each product type
  *
  * @param root    root node
  * @param options valid options names for the product
  *                type this root belongs to
  */
private[price] case class TreeRoot
(private[price] val root: TreeNode,
 private[price] val options: Seq[String])