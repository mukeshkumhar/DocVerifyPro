package com.example.docverifypro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResumeRecycleViewAdapter(private val resumeList: List<ResumeMongoData>): RecyclerView.Adapter<ResumeRecycleViewAdapter.ResumeViewHolder>() {
    class ResumeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.resumeName)
        val contactTextView: TextView = itemView.findViewById(R.id.resumeContact)
        val emailTextView: TextView = itemView.findViewById(R.id.resumeEmail)
        val addressTextView: TextView = itemView.findViewById(R.id.resumeAddress)
        val percentageTextView: TextView = itemView.findViewById(R.id.resumePercentage)
        val projectsRecyclerView: RecyclerView = itemView.findViewById(R.id.projectsRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.resume_recycle_view, parent, false)
        return ResumeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return resumeList.size
    }

    override fun onBindViewHolder(holder: ResumeViewHolder, position: Int) {
        val resume = resumeList[position]
        holder.nameTextView.text = resume.name
        holder.contactTextView.text = resume.contact
        holder.emailTextView.text = resume.email
        holder.addressTextView.text = resume.address
        holder.percentageTextView.text = resume.percentage.toString()

        val projectAdapter = ProjectAdapter(resume.projects)
        holder.projectsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.projectsRecyclerView.adapter = projectAdapter
    }
}