System.register(["@angular/platform-browser-dynamic", "./app.extractorroot"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var platform_browser_dynamic_1, app_extractorroot_1;
    return {
        setters:[
            function (platform_browser_dynamic_1_1) {
                platform_browser_dynamic_1 = platform_browser_dynamic_1_1;
            },
            function (app_extractorroot_1_1) {
                app_extractorroot_1 = app_extractorroot_1_1;
            }],
        execute: function() {
            platform_browser_dynamic_1.bootstrap(app_extractorroot_1.ExtractorRoot);
        }
    }
});
//# sourceMappingURL=main.js.map