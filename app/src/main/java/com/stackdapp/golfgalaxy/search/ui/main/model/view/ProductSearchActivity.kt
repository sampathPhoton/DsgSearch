package com.stackdapp.golfgalaxy.search.ui.main.model.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stackdapp.golfgalaxy.R
import com.stackdapp.search.view.ProductSearchFragment

class ProductSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_search_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductSearchFragment.newInstance())
                .commitNow()
        }
    }
}