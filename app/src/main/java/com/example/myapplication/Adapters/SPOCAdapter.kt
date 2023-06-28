package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.LoginSQL
import com.example.myapplication.LoginSQL.Companion
import com.example.myapplication.R
import com.example.myapplication.SuperUsersObjects

class SPOCAdapter(
    private val context: Context,
    private var dataset: MutableList<SuperUsersObjects>
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cmpName: TextView = view.findViewById(R.id.company_name_spoc)
        val HRname: TextView = view.findViewById(R.id.HR_name_spoc)
        val HREmail: TextView = view.findViewById(R.id.HREmails_Spoc)
        val roleSpoc: TextView = view.findViewById(R.id.role_spoc)
        var remarkEdit: EditText = view.findViewById(R.id.remark_spoc)
        var remarkBtn: Button = view.findViewById(R.id.remark_Btn)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentPos = dataset[position]
        val viewHolder = holder as? ViewHolder

        viewHolder!!.cmpName.text = currentPos.cName
        viewHolder.roleSpoc.text = currentPos.cRole
        viewHolder.HRname.text = currentPos.hrName
        viewHolder.HREmail.text = currentPos.hrEmail

        viewHolder.HREmail.text = currentPos.hrEmail

        viewHolder.remarkBtn.setOnClickListener {
            val remarkText = viewHolder.remarkEdit.text.toString().trim()
            if (remarkText.isEmpty()) {
                Toast.makeText(context, "No Remarks to send", Toast.LENGTH_SHORT).show()
            } else {
                sendRemark(currentPos.sEmail, remarkText)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.spoc_display, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun filterListByEmail(email: String) {
        val loginSQL = LoginSQL(context)
        val cursor = loginSQL.getName()

        // Clear the existing dataset
        dataset.clear()

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
                val newUser = SuperUsersObjects(company, role, userName, hrName, hrEmail, userEmail, "")
                dataset.add(newUser)
            }

            notifyDataSetChanged()
        }
    }
    private fun sendRemark(userEmail: String, remarkText: String) {
        val loginSQL = LoginSQL(context)
        val success = loginSQL.updateRemark(userEmail, remarkText)
        if (success) {
            Toast.makeText(context, "Remark sent successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to send remark", Toast.LENGTH_SHORT).show()
        }
    }
    }