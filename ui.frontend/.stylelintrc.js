/**
    No rules are turned on by default
    All the rules must be explicitly configured as there are no default values

    null - turn off the rule
    For more info: https://stylelint.io/user-guide/configuration/
 */

module.exports = {
	rules: {
		'at-rule-name-case': 'lower',
		'at-rule-name-space-after': 'always',
		'at-rule-no-vendor-prefix': true,
		'at-rule-semicolon-newline-after': 'always',
		'block-closing-brace-empty-line-before': 'never',
		'block-closing-brace-newline-before': 'always',
		'block-no-empty': null,
		'block-opening-brace-space-before': 'always',
		'color-no-invalid-hex': true,
		'declaration-bang-space-after': 'never',
		'declaration-bang-space-before': 'always',
		'declaration-block-no-duplicate-properties': true,
		'declaration-block-no-shorthand-property-overrides': true,
		'declaration-block-semicolon-newline-after': 'always',
		'declaration-block-semicolon-space-before': 'never',
		'declaration-block-trailing-semicolon': 'always',
		'declaration-colon-space-after': 'always',
		'declaration-colon-space-before': 'never',
		'declaration-no-important': true,
		'font-family-name-quotes': 'always-where-recommended',
		'font-family-no-duplicate-names': true,
		'function-url-quotes': 'always',
		indentation: ['tab', { except: ['value', 'param'], ignore: ['inside-parens'] }],
		'length-zero-no-unit': true,
		'max-empty-lines': 2,
		'media-feature-name-no-vendor-prefix': true,
		'no-duplicate-selectors': true,
		'no-eol-whitespace': true,
		'no-extra-semicolons': true,
		'no-missing-end-of-source-newline': true,
		'no-unknown-animations': true,
		'number-leading-zero': 'always',
		'number-no-trailing-zeros': true,
		'property-no-vendor-prefix': true,
		'selector-attribute-brackets-space-inside': 'never',
		'selector-attribute-operator-space-after': 'never',
		'selector-attribute-operator-space-before': 'never',
		'selector-attribute-quotes': 'always',
		'selector-combinator-space-after': 'always',
		'selector-combinator-space-before': 'always',
		'selector-no-qualifying-type': [true, {'ignore': ['attribute']}],
		'selector-no-vendor-prefix': true,
		'selector-pseudo-class-case': 'lower',
		'selector-pseudo-class-parentheses-space-inside': 'never',
		'selector-pseudo-element-case': 'lower',
		'selector-pseudo-element-colon-notation': 'double',
		'selector-type-case': 'lower',
		'string-quotes': 'single',
		'unit-case': 'lower',
		'value-keyword-case': 'lower',
		'value-list-max-empty-lines': 0,
		'value-no-vendor-prefix': true
	},
	overrides: [
    	{
      		"files": ["*.scss", "**/*.scss"],
      		"customSyntax": "postcss-scss"
    	}
  	]
};
