package in.newdevpoint.readcontacts;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.newdevpoint.readcontacts.databinding.RowContactListBinding;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyHolder> {

	private Context context;
	private ArrayList<ContactsProvider.Contact> filteredTagUserListModelArrayList;

	public ContactListAdapter(Context context, ArrayList<ContactsProvider.Contact> filteredTagUserListModelArrayList) {
		this.context = context;
		this.filteredTagUserListModelArrayList = filteredTagUserListModelArrayList;
	}

	@NonNull
	@Override
	public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		RowContactListBinding binding = DataBindingUtil.inflate(
				LayoutInflater.from(parent.getContext()),
				R.layout.row_contact_list, parent, false);
		return new MyHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
		ContactsProvider.Contact tagUserListModel = filteredTagUserListModelArrayList.get(position);
		holder.binding.rowContactName.setText(tagUserListModel.getDisplayName());

		ArrayList<ContactsProvider.Contact.Item> contacts = tagUserListModel.getPhones();

		StringBuilder contactsName = new StringBuilder();
		for (ContactsProvider.Contact.Item item : contacts) {
			contactsName.append(item.label).append(": ").append(item.value).append("\n");
		}


		holder.binding.rowContactMobile.setText(contactsName);
		if (!tagUserListModel.isHasPhoto()) {
			holder.binding.rowContactImage.setImageResource(R.drawable.ic_chat_contacts);
		} else {
			holder.binding.rowContactImage.setImageURI(Uri.parse(tagUserListModel.getPhotoUri()));
		}
	}


	public void addAll(ArrayList<ContactsProvider.Contact> arrayList) {
		this.filteredTagUserListModelArrayList.clear();
		this.filteredTagUserListModelArrayList.addAll(arrayList);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return filteredTagUserListModelArrayList.size();
	}


	public static class MyHolder extends RecyclerView.ViewHolder {

		private RowContactListBinding binding;

		public MyHolder(RowContactListBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}

	}
}
