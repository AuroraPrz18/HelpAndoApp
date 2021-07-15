package com.codepath.aurora.helpandoapp.viewModels;

import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {
    private int _typeUser = -1; // When -1, any option has been selected

    public int getTypeUser() {
        return _typeUser;
    }

    public void setTypeUser(int typeUser) {
        this._typeUser = typeUser;
    }
}
