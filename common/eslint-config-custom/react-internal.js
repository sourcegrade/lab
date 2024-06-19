const { resolve } = require("node:path");

const project = resolve(process.cwd(), "tsconfig.json");

/*
 * This is a custom ESLint configuration for use with
 * internal (bundled by their consumer) libraries
 * that utilize React.
 *
 * This config extends the Vercel Engineering Style Guide.
 * For more information, see https://github.com/vercel/style-guide
 *
 */

module.exports = {
    extends: [
        "@vercel/style-guide/eslint/browser",
        "@vercel/style-guide/eslint/typescript",
        "@vercel/style-guide/eslint/react",
    ].map(require.resolve),
    parserOptions: {
        project,
    },
    globals: {
        JSX: true,
    },
    settings: {
        "import/resolver": {
            typescript: {
                project,
            },
        },
    },
    ignorePatterns: ["node_modules/", "dist/", ".eslintrc.js", "__generated__"],

    rules: {
        "import/no-default-export": "off",
        "@typescript-eslint/no-unused-vars": "warn",
        "unicorn/filename-case": "warn",
        "import/no-extraneous-dependencies": "off", // since we define dependencies at top level this is not needed
        "import/no-default-export": "off",
        "@typescript-eslint/explicit-function-return-type": "off",
        "@typescript-eslint/no-explicit-any": "warn",
        "@typescript-eslint/no-unsafe-argument": "warn",
        "@typescript-eslint/unbound-method": "warn",
        "@typescript-eslint/no-unnecessary-condition": "warn",
        "@typescript-eslint/no-shadow": "warn",
        "@typescript-eslint/no-unsafe-assignment": "warn",
        "@typescript-eslint/no-unsafe-member-access": "warn",
        "@typescript-eslint/no-floating-promises": "warn",
        "@typescript-eslint/naming-convention": "warn",
        "@typescript-eslint/require-await": "warn",
        "@typescript-eslint/no-misused-promises": "off",
        "@typescript-eslint/no-empty-function": "warn",
        "@typescript-eslint/no-unsafe-return": "warn",
        "@typescript-eslint/no-unsafe-call": "warn",
        "camelcase": "warn",
        "no-console": "warn",
        "tsdoc/syntax": "off",
        "react/jsx-key": "off",
        "quotes": [
            "error",
            "double"
        ],
        "linebreak-style": [
            "error",
            "unix"
        ],
        "comma-dangle": [
            "error",
            {
                "arrays": "always-multiline",
                "objects": "always-multiline",
                "imports": "always-multiline",
                "exports": "always-multiline",
                "functions": "always-multiline"
            }
        ],
        "object-curly-spacing": [
            "warn",
            "always"
        ],
        "comma-spacing": [
            "warn",
            {
                "before": false,
                "after": true
            }
        ],
    },
};
