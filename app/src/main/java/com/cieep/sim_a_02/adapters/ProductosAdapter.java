package com.cieep.sim_a_02.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cieep.sim_a_02.MainActivity;
import com.cieep.sim_a_02.R;
import com.cieep.sim_a_02.modelos.Producto;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoVH> {

    private List<Producto> objects;
    private int resource; // XML de la fila (card)
    private Context context;
    private MainActivity main;

    private NumberFormat numberFormat;

    public ProductosAdapter(List<Producto> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        this.main = (MainActivity) context;
        numberFormat = NumberFormat.getCurrencyInstance();
    }

    @NonNull
    @Override
    public ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productoCardView = LayoutInflater.from(context).inflate(resource, null);
        productoCardView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ProductoVH(productoCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoVH holder, int position) {
        Producto p = objects.get(position);

        holder.lblPrecio.setText(numberFormat.format(p.getPrecio()));
        holder.lblCantidad.setText(String.valueOf(p.getCantidad()));
        holder.lblNombre.setText(p.getNombre());


        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete(p, holder.getAdapterPosition()).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProducto(p, holder.getAdapterPosition()).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private AlertDialog updateProducto(Producto p , int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(p.getNombre());
        builder.setCancelable(false);

        View editView = LayoutInflater.from(context).inflate(R.layout.activity_add_producto, null);
        EditText txtNombre = editView.findViewById(R.id.txtNombreProductoAdd);
        EditText txtCantidad = editView.findViewById(R.id.txtCantidadProductoAdd);
        EditText txtPrecio = editView.findViewById(R.id.txtPrecioProductoAdd);
        Button btnCrear = editView.findViewById(R.id.btnCrearProductoAdd);

        txtNombre.setEnabled(false);
        btnCrear.setVisibility(View.GONE);
        txtNombre.setText(p.getNombre());
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        txtPrecio.setText(String.valueOf(p.getPrecio()));

        builder.setView(editView);

        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("MODIFICAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()) {
                    p.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                    p.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                    notifyItemChanged(position);
                    main.calculaImportes();
                }
            }
        });

        return builder.create();
    }

    private AlertDialog confirmDelete(Producto p, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Seguro que quieres eliminar???");
        builder.setCancelable(false);

        builder.setNegativeButton("NO", null);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                objects.remove(p);
                notifyItemRemoved(position);
                main.calculaImportes();
            }
        });
        return builder.create();
    }

    public class ProductoVH extends RecyclerView.ViewHolder {

        TextView lblNombre;
        TextView lblCantidad;
        TextView lblPrecio;
        ImageButton btnEliminar;

        public ProductoVH(@NonNull View itemView) {
            super(itemView);

            lblNombre = itemView.findViewById(R.id.lblNombreProductoCard);
            lblCantidad = itemView.findViewById(R.id.lblCantidadProductoCard);
            lblPrecio = itemView.findViewById(R.id.lblPrecioProductoCard);
            btnEliminar = itemView.findViewById(R.id.btnEliminarProductoCard);
        }
    }
}
