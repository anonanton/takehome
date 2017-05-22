
Command line order price calculator
------------------------------------
Requires Java
------------------------------------
1) Use run.sh to start application. Otherwise work directory will not be set correctly
2) Follow prompts on the UI
    a) Configure can be used to change config at runtime.
        If left blank, the value will not be changed.
    b) Calculate takes path to a cart file.
        If path starts with "/" it will be treated as absolute.
        Otherwise path will be treated as relative to work dir.
        Sample cart files in data/cart/*.json
    c) Exit exits the application
