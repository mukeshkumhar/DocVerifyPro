package com.example.docverifypro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProjectAdapter (private val projects: List<MongoProject>) :
RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>(){
    class ProjectViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectNameTextView: TextView = itemView.findViewById(R.id.projectName)
        val projectSummaryTextView: TextView = itemView.findViewById(R.id.projectSummary)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectAdapter.ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.resume_project_recycle_view, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectAdapter.ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.projectNameTextView.text =project.projectName
        holder.projectSummaryTextView.text = project.projectSummary
    }

    override fun getItemCount(): Int {
        return projects.size
    }
}