package com.example.smarttasks.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Task(
    val id: String,
    @SerializedName("TargetDate") val targetDate: String,
    @SerializedName("DueDate") val dueDate: String?,
    @SerializedName("Title") val title: String,
    @SerializedName("Description") val description: String,
    @SerializedName("Priority") val priority: Int,
    var taskStatus: Int,
    var formattedStartDate: String,
    var formattedEndDate: String?,
    var comment: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(targetDate)
        parcel.writeString(dueDate)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeInt(priority)
        parcel.writeInt(taskStatus)
        parcel.writeString(formattedStartDate)
        parcel.writeString(formattedEndDate)
        parcel.writeString(comment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}