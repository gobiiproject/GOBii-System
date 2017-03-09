System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Link;
    return {
        setters: [],
        execute: function () {
            Link = (function () {
                function Link(href, description, allowedMethods) {
                    this.href = href;
                    this.description = description;
                    this.allowedMethods = allowedMethods;
                }
                Link.fromJSON = function (json) {
                    var href = json.href;
                    var description = json.description;
                    var allowedMethods = [];
                    json.methods.forEach(function (m) { allowedMethods.push(m); });
                    return new Link(href, description, allowedMethods);
                }; // fromJson()
                return Link;
            }());
            exports_1("Link", Link);
        }
    };
});
//# sourceMappingURL=link.js.map