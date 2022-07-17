package ch.m1m.scan;

public class KQuote {

    public String id;
    public int price;

    /**
     * Default constructor required for Jackson serializer
     */
    public KQuote() { }

    public KQuote(String id, int price) {
        this.id = id;
        this.price = price;
    }

    @Override
    public String toString() {
        return "KQuote{" +
                "id='" + id + '\'' +
                ", price=" + price +
                '}';
    }
}