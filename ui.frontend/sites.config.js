module.exports = [
    {
        'webpack_src_path':                 'componentLibrary',
        'webpack_copy_paths':               ['resources/base'],
        'ui_apps_destination':              'whitelabel',
        'ui_apps_clientlib_group':          'whitelabel',
        'ui_apps_clientlib_name':           'componentLibrary',
        'ui_apps_clientlib_dependencies':   ['whitelabel.dependencies']
    },
    {
        'webpack_src_path':                 'merkle',
        'webpack_copy_paths':               ['resources/base','resources/merkle'],
        'ui_apps_destination':              'whitelabel',
        'ui_apps_clientlib_group':          'whitelabel',
        'ui_apps_clientlib_name':           'merkle',
        'ui_apps_clientlib_dependencies':   ['whitelabel.dependencies']
    }
];
