package com.example.customdesign

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customdesign.adapter.ItemListener
import com.example.customdesign.adapter.MedicineAdapter
import com.example.customdesign.adapter.MedicineItemDetailLookUp
import com.example.customdesign.adapter.MedicineItemKeyProvider
import com.example.customdesign.data.Medicine
import com.example.customdesign.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Don't give arr
    private var medicineList = mutableListOf<Medicine>()

    private lateinit var binding: ActivityMainBinding


    var selectionTracker : SelectionTracker<Long>? = null

    //use as selectAll is more suitable
    var selectAll: Boolean = false

    //use like medicineAdapter and set private modifier
    private val medicineAdapter = MedicineAdapter(
        object : ItemListener {

            override fun onItemCountIncrease(item: Medicine) {
                item.count = item.count + 1
            }

            override fun onItemCountDecrease(item: Medicine) {
                if (item.count > 0) {
                    item.count = item.count - 1
                }
            }

            override fun getIfSelectAll(): Boolean {
                return selectAll
            }

        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicineList.add(Medicine("Biogestic", 1500, 0))
        medicineList.add(Medicine("Albendazole", 2000, 0))
        medicineList.add(Medicine("Cefditoren Pivoxil 100mg", 3500, 0))
        medicineList.add(Medicine("Hydroxychlogroquine Sulfate", 4000, 0))
        medicineList.add(Medicine("Tenofovir alafenamide", 8400, 0))
        medicineList.add(Medicine("ginkgo bilba phytosome", 33000, 0))

        binding.tvUnselectAll.setOnClickListener {
            selectionTracker?.clearSelection()
        }

        binding.tvSelectAll.setOnClickListener {
            medicineAdapter.currentList.forEach{ adapterList ->
                selectionTracker!!.select(medicineList.indexOf(adapterList).toLong())
            }

        }

        binding.rvMedicine.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = medicineAdapter
        }

        selectionTracker = SelectionTracker.Builder<Long>(
            "material",
            binding.rvMedicine,
            MedicineItemKeyProvider(binding.rvMedicine),
            MedicineItemDetailLookUp(binding.rvMedicine),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        selectionTracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>(){

                override fun onSelectionChanged() {
                    super.onSelectionChanged()

                    if (selectionTracker?.selection!!.size() > 0){
                        val data = selectionTracker?.selection!!.map {
                            medicineAdapter.currentList[it.toInt()]
                        }.toList()

                        var totalAmount : Int = 0

                       data.forEach{
                           totalAmount += it.price * it.count
                        }.toString()

                        binding.tvTotalPrice.text = totalAmount.toString() + "MMK"
                    }
                    else{
                        binding.tvTotalPrice.text = "${0} MMK"
                    }
                    
                }
            }
        )

        medicineAdapter.selectionTracker = selectionTracker
        medicineAdapter.submitList(medicineList)
        binding.tvTotalPrice.text = "${0} MMK"


    }
}