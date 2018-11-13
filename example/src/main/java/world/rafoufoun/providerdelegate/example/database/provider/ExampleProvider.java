package world.rafoufoun.providerdelegate.example.database.provider;


import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import world.rafoufoun.providerdelegate.DelegationProvider;
import world.rafoufoun.providerdelegate.ProviderDelegateManager;
import world.rafoufoun.providerdelegate.example.database.Contract;
import world.rafoufoun.providerdelegate.example.database.ExampleDatabase;
import world.rafoufoun.providerdelegate.example.database.delegate.example.ExampleDelegate;

public class ExampleProvider extends DelegationProvider {

    private ExampleDatabase database;

    @Override
    public void initializeProvider() {
        database = new ExampleDatabase(getContext());
    }

    @Override
    protected void addProviderDelegate(@NonNull ProviderDelegateManager delegateManager) {
        delegateManager.addDelegate(new ExampleDelegate());
    }

    @NonNull
    @Override
    protected String getAuthority() {
        return Contract.AUTHORITY;
    }

    @NonNull
    @Override
    protected SQLiteDatabase getWritableDatabase() {
        return database.getWritableDatabase();
    }

    @NonNull
    @Override
    protected SQLiteDatabase getReadableDatabase() {
        return database.getReadableDatabase();
    }
}
