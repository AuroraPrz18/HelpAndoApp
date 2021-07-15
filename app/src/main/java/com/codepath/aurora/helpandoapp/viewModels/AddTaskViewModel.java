package com.codepath.aurora.helpandoapp.viewModels;

import androidx.lifecycle.ViewModel;

public class AddTaskViewModel extends ViewModel {
    private int _category = -1; // When -1, any option has been selected

    public int getCategory() {
        return _category;
    }

    public void setCategory(int category) {
        this._category = category;
    }
}
