package com.gp.health.di.component;

import com.gp.health.di.module.DialogModule;
import com.gp.health.di.scope.DialogScope;

import dagger.Component;

@DialogScope
@Component(modules = DialogModule.class, dependencies = AppComponent.class)
public interface DialogComponent {


}
