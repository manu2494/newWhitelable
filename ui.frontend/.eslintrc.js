module.exports =  {
    parser:  '@typescript-eslint/parser',  // Specifies the ESLint parser
    extends:  [
      'plugin:@typescript-eslint/recommended',  // Uses the recommended rules from the @typescript-eslint/eslint-plugin
    ],
    parserOptions:  {
      ecmaVersion:  2018,  // Allows for the parsing of modern ECMAScript features
      sourceType:  'module',  // Allows for the use of imports
    },
    rules:  {
        "curly": 1,
        "@typescript-eslint/explicit-function-return-type": [0],
        "@typescript-eslint/no-explicit-any": [0],
        "ordered-imports": [0],
        "object-literal-sort-keys": [0],
        "max-len": [1, 1000],
        "new-parens": 1,
        "no-bitwise": 1,
        "no-cond-assign": 1,
        "no-trailing-spaces": 0,
        "eol-last": 1,
        // "func-style": ["error", "declaration", { "allowArrowFunctions": true }],
        "func-style": 0,
        "semi": 0,
        "curly": 0,
        "no-var": 0,
        "no-empty-function": 0,
        "@typescript-eslint/no-unused-vars": 0,
        "@typescript-eslint/no-empty-function": 0,
        "@typescript-eslint/no-extra-semi": 0,
        "@typescript-eslint/no-var-requires": 0
    },
  };
