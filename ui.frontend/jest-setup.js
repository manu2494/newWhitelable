const jest = require('@jest/globals');

global.jest = jest;
global.TextEncoder = require('util').TextEncoder;
global.TextDecoder = require('util').TextDecoder;
