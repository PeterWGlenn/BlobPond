package entities;

import java.util.Random;

public class Virus {

    public static void infect(Blob b) {

        Random r = new Random();
        int chance = r.nextInt(100);

        // If sick
        if (b.sick) {
            // Death
            if ((b.lifespan - b.sickAge) > 100) {
                b.kill();
            }
            // Infect
            if (chance < 50 && Blob.blobs.size() > 1) {
                Blob nearest = b.getClosestBlob();
                if (b.getDistance(nearest) < b.size / 2 + 20) {
                    nearest.sick = true;
                    nearest.sickAge = b.lifespan;
                }
            }
        }

    }

}
