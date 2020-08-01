<?php

namespace iAmir\Siadat\Providers;

use Illuminate\Routing\Router;
use Illuminate\Foundation\Support\Providers\RouteServiceProvider as ServiceProvider;

class RouteServiceProvider extends ServiceProvider
{
    public function boot()
    {
        parent::boot();
    }

    public function register()
    {
        parent::register();
    }

    public function map(Router $router)
    {
        $this->apiRoutes($router);
    }

    public function apiRoutes(Router $router)
    {
        $router->group([
            'namespace' => 'App\Http\Controllers\API',
            'prefix' => 'api',
            'middleware' => 'api'
        ], function ($router) {
            require_once(siadat_path('routes/api.php'));
        });
    }
}
