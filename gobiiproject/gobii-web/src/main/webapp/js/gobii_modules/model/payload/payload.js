System.register(["./links"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var links_1, Payload;
    return {
        setters: [
            function (links_1_1) {
                links_1 = links_1_1;
            }
        ],
        execute: function () {
            Payload = (function () {
                function Payload(data, links) {
                    this.data = data;
                    this.links = links;
                }
                Payload.fromJSON = function (json) {
                    var data = [];
                    json.data.forEach(function (d) {
                        data.push(d);
                    });
                    var links = links_1.Links.fromJSON(json.linkCollection);
                    return new Payload(data, links); // new
                }; // fromJson()
                return Payload;
            }());
            exports_1("Payload", Payload);
        }
    };
});
//# sourceMappingURL=payload.js.map