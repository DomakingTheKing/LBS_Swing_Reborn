public class Main {
    public static void main(String[] args) {
        Model JBSModel = new Model();
        View JBSView = new View();

        JBSModel.setView(JBSView);
        JBSView.setModel(JBSModel);


        Heart heart = new Heart();

        JBSModel.setHeart(heart);


        JBSView.start();

        Controller JBSController = new Controller(JBSView.getJpAttTot());
        JBSView.setKeyListener(JBSController);

        JBSController.setModel(JBSModel);

        JBSView.startGameView();

        JBSModel.setController(JBSController);

        JBSModel.start();

    }
}