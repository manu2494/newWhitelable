 /* eslint-disable @typescript-eslint/no-var-requires */

'use strict';

const path = require('path');
const sites = require('./sites.config.js');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const TSConfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');

const SOURCE_ROOT = __dirname + '/src/main/webpack';

const resolve = {
    extensions: ['.js', '.ts'],
    plugins: [new TSConfigPathsPlugin({
        configFile: './tsconfig.json'
    })]
};

module.exports = sites.map(site => {
    let plugins = [
        new CleanWebpackPlugin(),
        new ESLintPlugin({
            extensions: ['js', 'ts', 'tsx'],
            fix: true,
            failOnWarning: true,
            eslintPath: require.resolve('eslint'),
            formatter: 'checkstyle'
        }),
        new MiniCssExtractPlugin({
            filename: '[name].css'
        })
    ];
    if (Array.isArray(site.webpack_copy_paths) && site.webpack_copy_paths.length > 0) {
        const copyPatterns = site.webpack_copy_paths.map(copyPath => {
            return { from: path.resolve(__dirname, SOURCE_ROOT + '/' + copyPath), to: `./${copyPath}/` };
        });
        plugins.push(new CopyWebpackPlugin({ patterns: copyPatterns }));
    }

    return {
        name: site.webpack_src_path,
        resolve: resolve,
        entry: {
            [site.webpack_src_path]: SOURCE_ROOT + `/${site.webpack_src_path}/main.ts`,
        },
        output: {
            filename: '[name].js',
            path: `${__dirname}/dist/clientlib-${site.webpack_src_path}`
        },
        module: {
            rules: [
                {
                    test: /\.tsx?$/,
                    exclude: /node_modules/,
                    use: [
                        {
                            loader: 'ts-loader'
                        },
                        {
                            loader: 'glob-import-loader',
                            options: {
                                resolve: resolve
                            }
                        }
                    ]
                },
                {
                    test: /\.scss$/,
                    use: [
                        MiniCssExtractPlugin.loader,
                        {
                            loader: 'css-loader',
                            options: {
                                url: false
                            }
                        },
                        {
                            loader: 'postcss-loader',
                            options: {
                                plugins() {
                                    return [
                                        require('autoprefixer')
                                    ];
                                }
                            }
                        },
                        {
                            loader: 'sass-loader',
                        },
                        {
                            loader: 'glob-import-loader',
                            options: {
                                resolve: resolve
                            }
                        }
                    ]
                }
            ]
        },
        plugins: plugins,
        stats: {
            assetsSort: 'chunks',
            builtAt: true,
            children: false,
            chunkGroups: true,
            chunkOrigins: true,
            colors: false,
            errors: true,
            errorDetails: true,
            env: true,
            modules: false,
            performance: true,
            providedExports: false,
            source: false,
            warnings: true
        }
    };
});