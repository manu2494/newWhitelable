module.exports = {
    testEnvironment: 'jest-environment-jsdom',
    setupFiles: ['./jest-setup.js'],
    setupFilesAfterEnv: ['./jest-setup.js'],
    clearMocks: true,
    collectCoverage: true,
    coverageThreshold: {
        'global': {
            'branches': 50,
            'functions': 50,
            'lines': 50,
            'statements': 50
        }
    },
    coveragePathIgnorePatterns: [
        '<rootDir>/node_modules/',
        '<rootDir>/src/main/webpack/literegistration/components/js/literegistration/data/',
        '<rootDir>/src/main/webpack/whitelabel/components/js/recipe/data/'
    ]
};
