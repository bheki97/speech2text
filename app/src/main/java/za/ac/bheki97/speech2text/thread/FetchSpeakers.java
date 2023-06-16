package za.ac.bheki97.speech2text.thread;


public class FetchSpeakers extends Thread{

    private boolean flag;
    private FetchSpeakersCallback callback;

    public FetchSpeakers(FetchSpeakersCallback callback) {
        this.callback = callback;
        flag = true;
    }

    @Override
    public void run() {

        while(flag){
            callback.fetchAndUpdateSpeakers();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                stopCall();
                e.printStackTrace();
            }

            if (isInterrupted()) {
                return;
            }
        }

    }

    public void stopCall(){
        flag = false;
        interrupt();
    }
}
