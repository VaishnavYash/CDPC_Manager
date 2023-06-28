package com.example.myapplication.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapters.SuperuserAdapter
import com.example.myapplication.LoginSQL
import com.example.myapplication.R
import com.example.myapplication.SuperUsersObjects
import com.google.android.material.textfield.TextInputLayout

class SuperUserActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? =null
    val superDataset = arrayListOf<SuperUsersObjects>()
    val SQLDatabase = arrayListOf<DataBaseTable>()
    lateinit var superAdapter: SuperuserAdapter
    var AsnComp : Button ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_user)
        supportActionBar?.title = "Super User"
        var db = LoginSQL(this)

        recyclerView = findViewById(R.id.superuser_recycler)
        val layoutManager = LinearLayoutManager(this@SuperUserActivity)
        recyclerView?.layoutManager = layoutManager
        superAdapter = SuperuserAdapter(this, superDataset)
        recyclerView?.adapter = superAdapter

        // Clear the dataset
        superDataset.clear()

        AsnComp = findViewById(R.id.allot_cmp)
        AsnComp?.setOnClickListener {
            showPopupFormSuper()
        }
        loadDataFromDatabase()


    }
    private fun loadDataFromDatabase() {
        val db = LoginSQL(this)
        val cursor = db.getName()

        if (cursor != null) {
            val userNameIndex = cursor.getColumnIndex(LoginSQL.COL_USER_NAME)
            val userTypeIndex = cursor.getColumnIndex(LoginSQL.COL_USER_TYPE)
            val userEmailIndex = cursor.getColumnIndex(LoginSQL.COL_USER_EMAIL)
            val userPasswordIndex = cursor.getColumnIndex(LoginSQL.COL_USER_PASSWORD)
            val hrEmailIndex = cursor.getColumnIndex(LoginSQL.HR_EMAIL)
            val hrNameIndex = cursor.getColumnIndex(LoginSQL.HR_NAME)
            val companyIndex = cursor.getColumnIndex(LoginSQL.COMP_NAME)
            val roleIndex = cursor.getColumnIndex(LoginSQL.ROLE)
            val remarkIndex = cursor.getColumnIndex(LoginSQL.REMARK)

            while (cursor.moveToNext()) {
                val userName = if (userNameIndex >= 0) cursor.getString(userNameIndex) else ""
                val userType = if (userTypeIndex >= 0) cursor.getString(userTypeIndex) else ""
                val userEmail = if (userEmailIndex >= 0) cursor.getString(userEmailIndex) else ""
                val userPassword = if (userPasswordIndex >= 0) cursor.getString(userPasswordIndex) else ""
                val hrEmail = if (hrEmailIndex >= 0) cursor.getString(hrEmailIndex) else ""
                val hrName = if (hrNameIndex >= 0) cursor.getString(hrNameIndex) else ""
                val company = if (companyIndex >= 0) cursor.getString(companyIndex) else ""
                val role = if (roleIndex >= 0) cursor.getString(roleIndex) else ""
                val remark = if (remarkIndex >= 0) cursor.getString(remarkIndex) else ""

                if(company.isEmpty() && role.isEmpty()){
                    continue
                }
                val newUser = SuperUsersObjects(company, role, userName, hrName, hrEmail, userEmail, remark)
                superDataset.add(newUser)
            }
        }

        cursor?.close()
        superAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.superuser_menu, menu)

        // below line is to get our menu item.
        val searchItem: MenuItem = menu.findItem(R.id.search_icon)

        // getting search view of our item.
        val searchView: SearchView = searchItem.actionView as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
            }
        })
        return true
    }
    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<SuperUsersObjects> = ArrayList()

        // running a for loop to compare elements.
        for (item in superDataset) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.cName.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item)
            }
            if (item.cRole.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
            if (item.sName.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // Clear the current list
            superDataset.clear()
            // Add the filtered items to the list
            superDataset.addAll(filteredList)
            // Notify the adapter that the data has changed
            superAdapter.notifyDataSetChanged()
        }
    }

    private fun showPopupFormSuper() {

        val popupView = layoutInflater.inflate(R.layout.pop_form_super, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val pcNameEdit: EditText = popupView.findViewById(R.id.pop_Company_name_edit)
        val pcNameText: TextInputLayout = popupView.findViewById(R.id.pop_Company_name_text)
        val pspNameEdit: EditText = popupView.findViewById(R.id.pop_SPOC_name_edit)
        val pspNameText: TextInputLayout = popupView.findViewById(R.id.pop_SPOC_name_text)

        val pspEmailEdit: EditText = popupView.findViewById(R.id.pop_spocEMail_edit)
        val pspEmailText: TextInputLayout = popupView.findViewById(R.id.pop_spocEmail_text)

        val roleEdit: EditText = popupView.findViewById(R.id.pop_role_edit)
        val roleText: TextInputLayout = popupView.findViewById(R.id.pop_role_text)

        val HREdit: EditText = popupView.findViewById(R.id.pop_Company_HR_edit)
        val HRText: TextInputLayout = popupView.findViewById(R.id.pop_Company_HR_text)
        val HREmailEdit: EditText = popupView.findViewById(R.id.pop_Company_HREmail_edit)
        val HREmailText: TextInputLayout = popupView.findViewById(R.id.pop_Company_HREmail_text)

        val btn: Button = popupView.findViewById(R.id.btn_reg)

        btn.setOnClickListener {
            if(pcNameEdit.text.toString().trim().isEmpty()){
                pcNameText.error = "Name cannot be empty"
                return@setOnClickListener
            }
            if(HREdit.text.toString().trim().isEmpty()){
                HRText.error = "HR Name cannot be empty"
                return@setOnClickListener
            }
            if(HREmailEdit.text.toString().trim().isEmpty()){
            HREmailText.error = "HR Email cannot be empty"
            return@setOnClickListener
            }
            if(pspNameEdit.text.toString().trim().isEmpty()){
                pspNameText.error = "SPOC Name cannot be empty"
                return@setOnClickListener
            }
            if(pspEmailEdit.text.toString().trim().isEmpty()){
                pspEmailText.error = "SPOC Email ID cannot be empty"
                return@setOnClickListener
            }
            if(roleEdit.text.toString().trim().isEmpty()){
                roleText.error = "Please Specify the role of the company"
                return@setOnClickListener
            }

        val intent = intent
        var email: String? = intent.getStringExtra("key")
            var db = LoginSQL(this)
            db.addName(pspNameEdit.text.toString().trim(),"SPOC",
                "-","-",
                HREmailEdit.text.toString().trim(),HREdit.text.toString().trim(),
                pcNameEdit.text.toString().trim(),roleEdit.text.toString().trim(), "-")

            val newUser = SuperUsersObjects(pcNameEdit.text.toString().trim(),
                roleEdit.text.toString().trim(), pspNameEdit.text.toString().trim(),
                HREdit.text.toString().trim(), HREmailEdit.text.toString().trim(),
                pspEmailEdit.text.toString().trim(),"")

            insertDataIntoRecyclerView(newUser)

            Toast.makeText(this, "SPOC Alloted", Toast.LENGTH_SHORT).show()


            popupWindow.dismiss()
        }

    }



    private fun insertDataIntoRecyclerView(newUser: SuperUsersObjects) {
        superDataset.add(newUser)
        superAdapter.notifyItemInserted(superDataset.size - 1)
    }


}

