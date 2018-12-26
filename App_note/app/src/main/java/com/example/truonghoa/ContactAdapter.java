package com.example.truonghoa;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.truonghoa.app_note.R;

import org.w3c.dom.Text;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> listContact;
    private  OnCallBack listener;
    public ContactAdapter(OnCallBack listener,List<Contact> listContact) {
        this.listContact = listContact;
        this.listener = listener;
    }

    public List<Contact> getListContact() {
        return listContact;
    }

    public void setListContact(List<Contact> listContact) {
        this.listContact = listContact;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = listContact.get(position);
        holder.ivAvatar.setImageResource(Integer.parseInt(contact.getContact_avatar()));
        holder.tvContactName.setText(contact.getContact_fullname());
        holder.tvPhoneNumber.setText("Mobile: " + contact.getContact_phonenumber());
    }

    @Override
    public int getItemCount() {
        return listContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvContactName;
        TextView tvPhoneNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getPosition());
                }
            });
        }
    }
    public interface OnCallBack{
        void onItemClick(int position);
    }
}
