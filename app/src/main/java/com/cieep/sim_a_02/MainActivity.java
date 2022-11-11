package com.cieep.sim_a_02;

import android.content.Intent;
import android.os.Bundle;

import com.cieep.sim_a_02.adapters.ProductosAdapter;
import com.cieep.sim_a_02.modelos.Producto;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


import com.cieep.sim_a_02.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    // Lista Datos
    private ArrayList<Producto> productosList;

    // ActivityResultLaunchers
    private ActivityResultLauncher<Intent> addProductoLauncher;

    // RecyclerView
    private ProductosAdapter adapter;
    private RecyclerView.LayoutManager lm;

    private NumberFormat nf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        productosList = new ArrayList<>();
        inicializaLauncher();
        nf = NumberFormat.getCurrencyInstance();
        calculaImportes();

        setSupportActionBar(binding.toolbar);

        adapter = new ProductosAdapter(productosList, R.layout.producto_view_holder, this);
        lm  = new GridLayoutManager(this, 1);
        binding.contentMain.contentenedor.setAdapter(adapter);
        binding.contentMain.contentenedor.setLayoutManager(lm);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProductoActivity.class);
                addProductoLauncher.launch(intent);
            }
        });
    }

    private void inicializaLauncher() {
        addProductoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null && result.getData().getExtras() != null) {
                                Producto p = (Producto) result.getData().getExtras().getSerializable("PROD");
                                productosList.add(p);
                                adapter.notifyItemInserted(productosList.size()-1);
                                calculaImportes();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "NO HAY DATOS", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

    public void calculaImportes() {
        int cantidad = 0;
        float precio = 0;
        for (Producto p: productosList) {
            cantidad += p.getCantidad();
            precio += p.getPrecio() * p.getCantidad();
        }
        binding.contentMain.lblCantidad.setText(String.valueOf(cantidad));
        binding.contentMain.lblImporte.setText( nf.format(precio) );
    }

}