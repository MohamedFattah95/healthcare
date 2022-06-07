package com.gp.shifa.di.component;

import com.gp.shifa.di.module.DialogModule;
import com.gp.shifa.di.scope.DialogScope;

import dagger.Component;

@DialogScope
@Component(modules = DialogModule.class, dependencies = AppComponent.class)
public interface DialogComponent {


}
