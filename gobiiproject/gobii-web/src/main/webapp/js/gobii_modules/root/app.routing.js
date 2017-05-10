System.register(["@angular/router", "./app.extractorroot", "../views/login.component", "../services/core/auth.guard"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var router_1, app_extractorroot_1, login_component_1, auth_guard_1, appRoutes, routing;
    return {
        setters: [
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (app_extractorroot_1_1) {
                app_extractorroot_1 = app_extractorroot_1_1;
            },
            function (login_component_1_1) {
                login_component_1 = login_component_1_1;
            },
            function (auth_guard_1_1) {
                auth_guard_1 = auth_guard_1_1;
            }
        ],
        execute: function () {
            appRoutes = [
                { path: '', component: app_extractorroot_1.ExtractorRoot, canActivate: [auth_guard_1.AuthGuard] },
                { path: 'login', component: login_component_1.LoginComponent },
                //    { path: 'project', component: ExtractorRoot},
                //    { path: 'register', component: RegisterComponent },
                // otherwise redirect to home
                { path: '**', redirectTo: '' }
            ];
            exports_1("routing", routing = router_1.RouterModule.forRoot(appRoutes));
        }
    };
});
//# sourceMappingURL=app.routing.js.map