public enum TransferType {
    CBE_ACCOUNT("CBE Account", "Transfer to any CBE bank account"),
    OWN_ACCOUNT("Own Account", "Move money between your own CBE accounts"),
    CBE_AGENT("CBE Agent", "Transfer via CBE agent network"),
    CBE_WALLET("CBE Wallet", "Send to CBE mobile wallet"),
    OTHER_WALLET("Other Wallet", "Telebirr, M-Pesa and other wallets"),
    OTHER_BANK("Other Bank", "Transfer to Awash, Dashen, Abyssinia...");
    private final String displayName;
    private final String description;
    TransferType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getDescription() {
        return description;
    }
}