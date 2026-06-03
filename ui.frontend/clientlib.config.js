/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2020 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

 /* eslint-disable @typescript-eslint/no-var-requires */

const path = require('path');
const sites = require('./sites.config.js');

const BUILD_DIR = path.join(__dirname, 'dist');
const CLIENTLIB_ROOT = [
  __dirname,
  '..',
  'ui.apps',
  'src',
  'main',
  'content',
  'jcr_root',
  'apps',
  //'whitelabel',   ui.apps destination folder is now controlled in sites.config.js
  //'clientlibs'        ui.apps destination folder is now controlled in sites.config.js
];

const libsBaseConfig = {
  allowProxy: true,
  serializationFormat: 'xml',
  cssProcessor: ['default:none', 'min:none'],
  jsProcessor: ['default:none', 'min:none']
};

// Config for `aem-clientlib-generator`
const config = {
  context: BUILD_DIR,
  clientLibRoot: path.join(...CLIENTLIB_ROOT),
  libs: []
};

sites.forEach(site => {
    let siteconfig = {
        ...libsBaseConfig,
        name: path.join(site.ui_apps_destination, 'clientlibs', `clientlib-${site.ui_apps_clientlib_name}`),
        dependencies: [...site.ui_apps_clientlib_dependencies],
        categories: [`${site.ui_apps_clientlib_group}.${site.ui_apps_clientlib_name}`],
        assets: {
          js: {
            cwd:  `clientlib-${site.webpack_src_path}`,
            files: ['**/*.js'],
            flatten: false
          },
          css: {
            cwd:  `clientlib-${site.webpack_src_path}`,
            files: ['**/*.css'],
            flatten: false
          }
        }  
    }
    if (Array.isArray(site.webpack_copy_paths)) {
        site.webpack_copy_paths.forEach((copyPath) => {
            siteconfig.assets[`${copyPath}`] = {
                cwd:  `clientlib-${site.webpack_src_path}`,
                base: '.',
                files: [`${copyPath}/**/*.*`],
                flatten: false,
                ignore: ['**/*.js', '**/*.css']
            };
        });
    }
    config.libs.push(siteconfig);
});

module.exports = config;
