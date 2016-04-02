package io.github.ryanhoo.firFlight.account;

import android.content.Context;
import android.content.SharedPreferences;
import io.github.ryanhoo.firFlight.data.model.User;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 4/2/16
 * Time: 5:42 PM
 * Desc: AccountManager
 */
public class AccountManager {

    private static final String PREFS_ACCOUNTS = "accounts.xml";

    private static final String KEY_CURRENT_ACCOUNT = "current";
    private static final String KEY_ACCOUNTS = "accounts";

    public static void addAccount(Context context, Account account) {
        // TODO Event: Add Account
        SharedPreferences prefs = context.getSharedPreferences(PREFS_ACCOUNTS, Context.MODE_PRIVATE);
        Set<String> accountSet = prefs.getStringSet(KEY_ACCOUNTS, null);
        if (accountSet == null) {
            accountSet = new LinkedHashSet<>();
        }
        if (!accountSet.contains(account.getName())) {
            accountSet.add(account.getName());
        }
        prefs.edit().putString(KEY_CURRENT_ACCOUNT, account.getName())
                .putStringSet(KEY_ACCOUNTS, accountSet).apply();
    }

    public static Account getCurrentAccount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_ACCOUNTS, Context.MODE_PRIVATE);
        String accountName = prefs.getString(KEY_CURRENT_ACCOUNT, null);
        return new Account(accountName);
    }

    public static List<Account> getAccounts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_ACCOUNTS, Context.MODE_PRIVATE);
        Set<String> accountSet = prefs.getStringSet(KEY_ACCOUNTS, null);
        if (accountSet != null) {
            List<Account> accounts = new ArrayList<>(accountSet.size());
            for (String accountName : accountSet) {
                Account account = new Account(accountName);
                User user = UserSession.getInstance().getUserOfAccount(account);
                account.setUser(user);
                accounts.add(account);
            }
            return accounts;
        }
        return new ArrayList<>(0);
    }

    public static boolean removeAccount(Context context, Account account) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_ACCOUNTS, Context.MODE_PRIVATE);
        Set<String> accountSet = prefs.getStringSet(KEY_ACCOUNTS, null);
        if (accountSet != null && accountSet.contains(account.getName())) {
            return accountSet.remove(account.getName());
        }
        return false;
    }
}
