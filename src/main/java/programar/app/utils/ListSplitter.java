package programar.app.utils;

import programar.app.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ListSplitter {


    public static List<List<Product>> splitListIntoSublistsOfFour(List<Product> ofertas) {
        return splitListIntoSublists(ofertas, 4);
    }

    public static List<List<Product>> splitListIntoSublistsOfTwo(List<Product> ofertas) {
        return splitListIntoSublists(ofertas, 2);
    }

    private static List<List<Product>> splitListIntoSublists(List<Product> originalList, int sublistSize) {
        List<List<Product>> sublists = new ArrayList<>();
        int totalSize = originalList.size();

        for (int i = 0; i < totalSize; i += sublistSize) {
            sublists.add(originalList.subList(i, Math.min(totalSize, i + sublistSize)));
        }

        return sublists;
    }
}
