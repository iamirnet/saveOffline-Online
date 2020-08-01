<?php

namespace iAmir\Siadat\Providers;

use Illuminate\Support\ServiceProvider;

class AppServiceProvider extends ServiceProvider
{
    public function boot()
    {
        $this->mergeConfigFrom(siadat_path('config/siadat.php'), 'iamir.siadat');

        if($this->app->runningInConsole())
        {
            $this->loadMigrationsFrom(siadat_path('database/migrations'));
        }
    }

    public function register()
    {
        parent::register();
    }
}
