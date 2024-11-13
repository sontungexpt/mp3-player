package com.bt3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ComponentActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends ComponentActivity {

  private static final int REQUEST_CONTACTS_PERMISSION = 1;

  private List<Contact> contactList;
  private ContactsAdapter contactsAdapter;
  private RecyclerView recyclerView;
  private List<Long> selectedContactIds = new ArrayList<>();
  private boolean isMultiSelectEnabled = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView = findViewById(R.id.recyclerView);
    contactList = new ArrayList<>();
    contactsAdapter = new ContactsAdapter(contactList);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(contactsAdapter);

    Button btnAddContact = findViewById(R.id.btnAddContact);
    btnAddContact.setOnClickListener(v -> showAddContactDialog());

    checkPermissions();
  }

  private void checkPermissions() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
          this,
          new String[] {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
          REQUEST_CONTACTS_PERMISSION);
    } else {
      loadContacts();
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == REQUEST_CONTACTS_PERMISSION) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        loadContacts();
      } else {
        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void loadContacts() {
    contactList.clear();
    Cursor cursor =
        getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

    if (cursor != null && cursor.getCount() > 0) {
      while (cursor.moveToNext()) {
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        String name =
            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        String phoneNumber = "";

        Cursor phones =
            getContentResolver()
                .query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[] {id},
                    null);
        if (phones != null) {
          while (phones.moveToNext()) {
            phoneNumber =
                phones.getString(
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          }
          phones.close();
        }

        contactList.add(new Contact(name, phoneNumber));
      }
      cursor.close();
    }

    contactsAdapter.notifyDataSetChanged();
  }

  private void showAddContactDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Thêm danh bạ");

    // Layout cho dialog
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_contact, null);
    builder.setView(dialogView);

    final EditText nameInput = dialogView.findViewById(R.id.name_input);
    final EditText phoneInput = dialogView.findViewById(R.id.phone_input);

    builder.setPositiveButton(
        "Thêm",
        (dialog, which) -> {
          String name = nameInput.getText().toString();
          String phone = phoneInput.getText().toString();
          addContact(name, phone);
        });

    builder.setNegativeButton("Hủy", null);
    builder.show();
  }

  private void addContact(String name, String phone) {
    ContentValues values = new ContentValues();
    values.put(ContactsContract.RawContacts.ACCOUNT_TYPE, true);
    values.put(ContactsContract.RawContacts.ACCOUNT_NAME, true);
    Uri rawContactUri =
        getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
    long rawContactId = ContentUris.parseId(rawContactUri);

    // Thêm tên
    values.clear();
    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
    values.put(
        ContactsContract.Data.MIMETYPE,
        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
    values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
    getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

    // Thêm số điện thoại
    values.clear();
    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
    values.put(
        ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
    values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
    values.put(
        ContactsContract.CommonDataKinds.Phone.TYPE,
        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
    getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

    loadContacts(); // Tải lại danh bạ
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.context_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();
    if (itemId == R.id.sort_asc) {
      Collections.sort(contactList, Comparator.comparing(Contact::getName));
      contactsAdapter.notifyDataSetChanged();
      return true;
    } else if (itemId == R.id.sort_desc) {
      Collections.sort(contactList, (c1, c2) -> c2.getName().compareTo(c1.getName()));
      contactsAdapter.notifyDataSetChanged();
      return true;
    } else if (itemId == R.id.delete) {
      // Xóa danh bạ đã chọn
      deleteSelectedContacts();
      return true;
    } else if (itemId == R.id.select_multiple) {
      enableMultipleSelection();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void deleteSelectedContacts() {
    if (selectedContactIds.isEmpty()) {
      Toast.makeText(this, "Chưa chọn danh bạ để xóa", Toast.LENGTH_SHORT).show();
      return;
    }

    for (Long contactId : selectedContactIds) {
      Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
      getContentResolver().delete(uri, null, null);
    }
    loadContacts(); // Tải lại danh bạ
    selectedContactIds.clear();
    isMultiSelectEnabled = false;
  }

  private void enableMultipleSelection() {
    isMultiSelectEnabled = !isMultiSelectEnabled;

    if (isMultiSelectEnabled) {
      // Hiển thị checkbox cho mỗi danh bạ
      // Cập nhật RecyclerView với chế độ chọn nhiều
    } else {
      // Ẩn checkbox và dọn dẹp danh sách đã chọn
      selectedContactIds.clear();
    }
  }
}
