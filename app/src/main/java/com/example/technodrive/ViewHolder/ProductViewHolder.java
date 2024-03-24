package com.example.technodrive.ViewHolder;

import android.text.style.TtsSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.technodrive.Interface.ItemClickListner;
import com.example.technodrive.R;

public class ProductViewHolder {
    public TextView title,count,mark,desck,price;
    public ImageView imageProduct;
    public ItemClickListner listner;


    public ProductViewHolder(View itemView)
    {
        super(itemView);
        title=itemView.findViewById(R.id.textViewInfoProductTitle);
        count=itemView.findViewById(R.id.textViewInfoProductCount);
        mark=itemView.findViewById(R.id.textViewInfoProductMark);
        desck=itemView.findViewById(R.id.textViewIbfoProductDesckription);
        price=itemView.findViewById(R.id.textViewInfoProductPrice);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
