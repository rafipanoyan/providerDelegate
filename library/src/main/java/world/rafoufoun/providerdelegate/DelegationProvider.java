package world.rafoufoun.providerdelegate;

import android.content.ContentProvider;

/**
 * A ready to go {@link ContentProvider} implementation having a {@link ProviderDelegateManager}
 * ready to use. You can call {@link ProviderDelegateManager#addDelegate(ProviderDelegate)}
 * at any time if you have called {@link #onCreate()} into your subclass {@link DelegationProvider#onCreate()}
 */
public abstract class DelegationProvider extends ContentProvider {

    protected ProviderDelegateManager delegateManager;

    /**
     * Initialize the ProviderDelegateManager.
     * You MUST call this method at very FIRST line into your overridden method.
     * Add your {@link ProviderDelegate} here.
     *
     * @return return true if the initialization is ok.
     */
    @Override
    public boolean onCreate() {
        delegateManager = new ProviderDelegateManager(getContext(), getAuthority());
        return true;
    }

    /**
     * Get the authority for this provider
     *
     * @return return the provider authority
     */
    protected abstract String getAuthority();
}
