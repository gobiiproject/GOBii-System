System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ServerConfig;
    return {
        setters:[],
        execute: function() {
            ServerConfig = (function () {
                function ServerConfig(crop, domain, contextRoot, port) {
                    this.crop = crop;
                    this.domain = domain;
                    this.contextRoot = contextRoot;
                    this.port = port;
                    this.crop = crop;
                    this.domain = domain;
                    this.port = port;
                }
                return ServerConfig;
            }());
            exports_1("ServerConfig", ServerConfig);
        }
    }
});
//# sourceMappingURL=server-config.js.map