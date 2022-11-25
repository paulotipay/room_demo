package com.kodego.app.roomdemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.app.roomdemo.databinding.ActivityMainBinding
import com.kodego.app.roomdemo.databinding.UpdateDialogBinding
import com.kodego.app.roomdemo.db.CompanyDatabase
import com.kodego.app.roomdemo.db.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var companyDB : CompanyDatabase
    lateinit var adapter : EmployeeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        companyDB = CompanyDatabase.invoke(this)

        //display table data on screen
        view()

        binding.btnSave.setOnClickListener(){
            var name:String = binding.etName.text.toString()
            var salary:Int = binding.etSalary.text.toString().toInt()

            val employee = Employee(name, salary)
            save(employee)
            adapter.employeeModel.add(employee)
            adapter.notifyDataSetChanged()

            Toast.makeText(applicationContext,"Saved!", Toast.LENGTH_LONG).show()
        }

    }

    private fun delete(employee:Employee){
        GlobalScope.launch(Dispatchers.IO) {
            companyDB.getEmployees().deleteEmployee(employee.id)
            view()
        }
    }


    private fun view() {
        lateinit var employee: MutableList<Employee>
        GlobalScope.launch(Dispatchers.IO) {
            employee = companyDB.getEmployees().getAllEmployees()

            withContext(Dispatchers.Main){
                adapter = EmployeeAdapter(employee)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)

                adapter.onItemDelete = { item:Employee, position: Int ->

                    delete(item)
                    adapter.employeeModel.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                adapter.onUpdate =  { item:Employee, position: Int ->

                    showUpdateDialog(item.id)
                    adapter.notifyDataSetChanged()
                }

            }
        }

    }


    private fun save(employee: Employee) {
        GlobalScope.launch(Dispatchers.IO) {
            companyDB.getEmployees().addEmployee(employee)
            view()
        }
    }

    private fun showUpdateDialog(id:Int) {
        val dialog = Dialog(this)
        val binding: UpdateDialogBinding = UpdateDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnOK.setOnClickListener(){
            var newName :String = binding.etNewName.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                companyDB.getEmployees().updateEmployee(newName,id)
                view()
            }
            dialog.dismiss()
        }

    }

}