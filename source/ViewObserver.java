
interface ViewObserver {

    public void onDrawNotification(State state);

    public void onCounterNotification(int[] stats);

    public void onTextNotification(String message);

    public void onClearGraphNotification();

    public void onQuitNotification();

}