package com.example.ecommerce.Sellers;



import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SellerProductCategoryFragment extends Fragment {


    public SellerProductCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_product_category, container, false);
    }

}
