package in.newdevpoint.readcontacts;

import android.Manifest;
import android.content.ContentResolver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PermissionClass.PermissionRequire {


	private static final int REQUEST_READ_CONTACTS = 23;
	private ArrayList<ContactsProvider.Contact> tagUserListModelArrayList = new ArrayList<>();
	private ContactListAdapter contactListAdapter;
	private PermissionClass permissionClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		in.newdevpoint.readcontacts.databinding.ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


		contactListAdapter = new ContactListAdapter(MainActivity.this, tagUserListModelArrayList);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
		binding.contactRecyclerView.setLayoutManager(mLayoutManager);
		binding.contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
		binding.contactRecyclerView.setAdapter(contactListAdapter);


		permissionClass = new PermissionClass(this, this);
		permissionClass.askPermission(REQUEST_READ_CONTACTS);
	}


	private void getAllContacts() {
		AsyncTask<Void, Void, ArrayList<ContactsProvider.Contact>> myAsyncTask = new AsyncTask<Void, Void, ArrayList<ContactsProvider.Contact>>() {

			@Override
			protected void onPostExecute(ArrayList<ContactsProvider.Contact> contacts) {
				contactListAdapter.addAll(contacts);
			}

			@Override
			protected ArrayList<ContactsProvider.Contact> doInBackground(final Void... params) {

				ContentResolver cr = getContentResolver();

				ContactsProvider contactsProvider = new ContactsProvider(cr);
				ArrayList<ContactsProvider.Contact> contacts = contactsProvider.getContacts();


				System.out.println("--------------------------------");


//				System.out.println(contacts);
				return contacts;

			}
		};
		myAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		permissionClass.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void permissionDeny() {
	}

	@Override
	public void permissionGranted(int flag) {
		switch (flag) {
			case REQUEST_READ_CONTACTS:

				Toast.makeText(this, "Permission allow", Toast.LENGTH_SHORT).show();
				/*new ContactUtility(this, new ContactUtility.ReadContacts() {
					@Override
					public void contactList(ArrayList<ContactModel> contactModels) {
						contactListAdapter.addAll(contactModels);
					}
				}).getContactList();*/

				getAllContacts();
				break;

		}
	}

	@Override
	public String[] listOfPermission(int flag) {
		return new String[]{Manifest.permission.READ_CONTACTS};
	}
}
