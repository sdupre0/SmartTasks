package com.example.smarttasks.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Task(
    val id: String?,

    @SerializedName("TargetDate")
    val targetDate: String?,

    @SerializedName("DueDate")
    val dueDate: String?,

    @SerializedName("Title")
    val title: String?,

    @SerializedName("Description")
    val description: String?,

    @SerializedName("Priority")
    val priority: Int
): Parcelable {
    constructor(parcel: Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(targetDate)
        parcel.writeString(dueDate)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeInt(priority)
    }

    companion object CREATOR: Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}

data class TaskResponse (
    @SerializedName("tasks")
    val tasks: List<Task>
)