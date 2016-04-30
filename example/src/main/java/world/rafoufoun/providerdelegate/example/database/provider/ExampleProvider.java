package world.rafoufoun.providerdelegate.example.database.provider;


import android.database.sqlite.SQLiteDatabase;

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
    protected void addProviderDelegate(ProviderDelegateManager delegateManager) {
        delegateManager.addDelegate(new ExampleDelegate(getAuthority()));
    }

    @Override
    protected String getAuthority() {
        return Contract.AUTHORITY;
    }

    @Override
    protected SQLiteDatabase getWritableDatabase() {
        return database.getWritableDatabase();
    }

    @Override
    protected SQLiteDatabase getReadableDatabase() {
        return database.getReadableDatabase();
    }
}
