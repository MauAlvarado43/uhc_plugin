package vch.uhc.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UHC Spawn Distribution Algorithm - Lógica Compleja")
class UHCSpawnAlgorithmTest {

    private static class SpawnPoint {
        final int x;
        final int z;

        SpawnPoint(int x, int z) {
            this.x = x;
            this.z = z;
        }

        double distanceTo(SpawnPoint other) {
            int dx = this.x - other.x;
            int dz = this.z - other.z;
            return Math.sqrt(dx * dx + dz * dz);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SpawnPoint that = (SpawnPoint) o;
            return x == that.x && z == that.z;
        }

        @Override
        public int hashCode() {
            return 31 * x + z;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + z + ")";
        }
    }

    private static List<SpawnPoint> calculateSpawns(int spawnCount, int size) {
        List<SpawnPoint> spawns = new ArrayList<>();

        if (spawnCount == 0) {
            return spawns;
        }

        if (spawnCount == 2) {
            spawns.add(new SpawnPoint(-size, -size));
            spawns.add(new SpawnPoint(size, size));
            return spawns;
        }

        if (spawnCount == 4) {
            spawns.add(new SpawnPoint(-size, -size));
            spawns.add(new SpawnPoint(size, -size));
            spawns.add(new SpawnPoint(size, size));
            spawns.add(new SpawnPoint(-size, size));
            return spawns;
        }

        spawns.add(new SpawnPoint(-size, -size));
        spawns.add(new SpawnPoint(size, -size));
        spawns.add(new SpawnPoint(size, size));
        spawns.add(new SpawnPoint(-size, size));

        if (spawnCount <= 4) {
            return new ArrayList<>(spawns.subList(0, spawnCount));
        }

        int remainingSpawns = spawnCount - 4;
        int[] pointsPerSide = new int[4];
        for (int i = 0; i < remainingSpawns; i++) {
            pointsPerSide[i % 4]++;
        }

        // Lado Norte
        for (int i = 1; i <= pointsPerSide[0]; i++) {
            double fraction = i / (double)(pointsPerSide[0] + 1);
            int x = (int)(-size + 2 * size * fraction);
            spawns.add(new SpawnPoint(x, -size));
        }

        // Lado Este
        for (int i = 1; i <= pointsPerSide[1]; i++) {
            double fraction = i / (double)(pointsPerSide[1] + 1);
            int z = (int)(-size + 2 * size * fraction);
            spawns.add(new SpawnPoint(size, z));
        }

        // Lado Sur
        for (int i = 1; i <= pointsPerSide[2]; i++) {
            double fraction = i / (double)(pointsPerSide[2] + 1);
            int x = (int)(size - 2 * size * fraction);
            spawns.add(new SpawnPoint(x, size));
        }

        // Lado Oeste
        for (int i = 1; i <= pointsPerSide[3]; i++) {
            double fraction = i / (double)(pointsPerSide[3] + 1);
            int z = (int)(size - 2 * size * fraction);
            spawns.add(new SpawnPoint(-size, z));
        }

        return spawns;
    }

    @Nested
    @DisplayName("Casos Especiales - 2 y 4 Spawns")
    class SpecialCasesTests {

        @Test
        @DisplayName("2 spawns: esquinas opuestas en diagonal")
        void twoSpawnsShouldBeOppositeCorners() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(2, size);

            assertThat(spawns).hasSize(2);

            SpawnPoint spawn1 = spawns.get(0);
            SpawnPoint spawn2 = spawns.get(1);

            assertThat(spawn1).isEqualTo(new SpawnPoint(-size, -size));
            assertThat(spawn2).isEqualTo(new SpawnPoint(size, size));

            double expectedDistance = Math.sqrt(2) * 2 * size;
            double actualDistance = spawn1.distanceTo(spawn2);
            assertThat(actualDistance).isCloseTo(expectedDistance, within(0.1));
        }

        @Test
        @DisplayName("4 spawns: las cuatro esquinas del rectángulo")
        void fourSpawnsShouldBeFourCorners() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(4, size);

            assertThat(spawns).hasSize(4);

            assertThat(spawns).containsExactlyInAnyOrder(
                new SpawnPoint(-size, -size),
                new SpawnPoint(size, -size),
                new SpawnPoint(size, size),
                new SpawnPoint(-size, size)
            );
        }

        @Test
        @DisplayName("4 spawns: distancias entre esquinas adyacentes son iguales")
        void fourSpawnsAdjacentDistancesShouldBeEqual() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(4, size);

            SpawnPoint nw = spawns.get(0);
            SpawnPoint ne = spawns.get(1);
            SpawnPoint se = spawns.get(2);
            SpawnPoint sw = spawns.get(3);

            double dist_nw_ne = nw.distanceTo(ne);
            double dist_ne_se = ne.distanceTo(se);
            double dist_se_sw = se.distanceTo(sw);
            double dist_sw_nw = sw.distanceTo(nw);

            double expectedSideLength = 2.0 * size;

            assertThat(dist_nw_ne).isCloseTo(expectedSideLength, within(0.1));
            assertThat(dist_ne_se).isCloseTo(expectedSideLength, within(0.1));
            assertThat(dist_se_sw).isCloseTo(expectedSideLength, within(0.1));
            assertThat(dist_sw_nw).isCloseTo(expectedSideLength, within(0.1));
        }

        @Test
        @DisplayName("2 spawns con diferentes tamaños de mapa")
        void twoSpawnsWithDifferentMapSizes() {
            for (int size : List.of(500, 1000, 1500, 2000)) {
                List<SpawnPoint> spawns = calculateSpawns(2, size);

                assertThat(spawns).hasSize(2);
                assertThat(spawns.get(0)).isEqualTo(new SpawnPoint(-size, -size));
                assertThat(spawns.get(1)).isEqualTo(new SpawnPoint(size, size));
            }
        }
    }

    @Nested
    @DisplayName("Distribución General en Perímetro (5+ spawns)")
    class GeneralPerimeterDistributionTests {

        @Test
        @DisplayName("5 spawns: 4 esquinas + 1 adicional en perímetro")
        void fiveSpawnsShouldIncludeFourCornersPlusOne() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(5, size);

            assertThat(spawns).hasSize(5);

            assertThat(spawns.subList(0, 4)).containsExactlyInAnyOrder(
                new SpawnPoint(-size, -size),
                new SpawnPoint(size, -size),
                new SpawnPoint(size, size),
                new SpawnPoint(-size, size)
            );

            SpawnPoint fifth = spawns.get(4);
            boolean onPerimeter = 
                fifth.x == -size || fifth.x == size || 
                fifth.z == -size || fifth.z == size;
            assertThat(onPerimeter).isTrue();
        }

        @Test
        @DisplayName("8 spawns: 4 esquinas + 4 en lados (1 por lado)")
        void eightSpawnsShouldDistributeEvenly() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(8, size);

            assertThat(spawns).hasSize(8);

            assertThat(spawns.subList(0, 4)).containsExactlyInAnyOrder(
                new SpawnPoint(-size, -size),
                new SpawnPoint(size, -size),
                new SpawnPoint(size, size),
                new SpawnPoint(-size, size)
            );

            for (int i = 4; i < 8; i++) {
                SpawnPoint spawn = spawns.get(i);
                boolean onPerimeter = 
                    spawn.x == -size || spawn.x == size || 
                    spawn.z == -size || spawn.z == size;
                assertThat(onPerimeter)
                    .withFailMessage("Spawn %s debe estar en el perímetro", spawn)
                    .isTrue();
            }
        }

        @Test
        @DisplayName("12 spawns: distribución uniforme en perímetro")
        void twelveSpawnsShouldDistributeUniformly() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(12, size);

            assertThat(spawns).hasSize(12);

            for (SpawnPoint spawn : spawns) {
                boolean onPerimeter = 
                    spawn.x == -size || spawn.x == size || 
                    spawn.z == -size || spawn.z == size;
                assertThat(onPerimeter)
                    .withFailMessage("Spawn %s debe estar en el perímetro", spawn)
                    .isTrue();
            }
        }

        @Test
        @DisplayName("20 spawns: ningún spawn repetido")
        void twentySpawnsShouldHaveNoDuplicates() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(20, size);

            assertThat(spawns).hasSize(20);

            Set<SpawnPoint> uniqueSpawns = new HashSet<>(spawns);
            assertThat(uniqueSpawns).hasSize(20);
        }
    }

    @Nested
    @DisplayName("Análisis de Equidad de Distancias")
    class DistanceFairnessTests {

        @Test
        @DisplayName("2 spawns: distancia debe ser máxima posible")
        void twoSpawnsMaximumDistance() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(2, size);

            double distance = spawns.get(0).distanceTo(spawns.get(1));
            double expectedMaxDistance = Math.sqrt(2) * 2 * size;

            assertThat(distance).isCloseTo(expectedMaxDistance, within(1.0));
        }

        @Test
        @DisplayName("4 spawns: distancias diagonales vs adyacentes")
        void fourSpawnsDiagonalVsAdjacentDistances() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(4, size);

            SpawnPoint nw = spawns.get(0);
            SpawnPoint ne = spawns.get(1);
            SpawnPoint se = spawns.get(2);

            double adjacentDistance = nw.distanceTo(ne);

            double diagonalDistance = nw.distanceTo(se);

            assertThat(diagonalDistance / adjacentDistance).isCloseTo(Math.sqrt(2), within(0.01));
        }

        @Test
        @DisplayName("8 spawns: distancia mínima entre spawns")
        void eightSpawnsMinimumDistance() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(8, size);

            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < spawns.size(); i++) {
                for (int j = i + 1; j < spawns.size(); j++) {
                    double dist = spawns.get(i).distanceTo(spawns.get(j));
                    minDistance = Math.min(minDistance, dist);
                }
            }

            assertThat(minDistance).isGreaterThan(500);
        }

        @Test
        @DisplayName("Verificar distribución uniforme con desviación estándar")
        void verifyUniformDistributionWithStdDev() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(12, size);

            double avgDistanceToCenter = spawns.stream()
                .mapToDouble(sp -> Math.sqrt(sp.x * sp.x + sp.z * sp.z))
                .average()
                .orElse(0);

            double variance = spawns.stream()
                .mapToDouble(sp -> {
                    double dist = Math.sqrt(sp.x * sp.x + sp.z * sp.z);
                    double diff = dist - avgDistanceToCenter;
                    return diff * diff;
                })
                .average()
                .orElse(0);

            double stdDev = Math.sqrt(variance);

            assertThat(stdDev).isLessThan(avgDistanceToCenter * 0.3);
        }
    }

    @Nested
    @DisplayName("Casos Límite y Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("0 spawns: lista vacía")
        void zeroSpawnsShouldReturnEmptyList() {
            List<SpawnPoint> spawns = calculateSpawns(0, 1000);
            assertThat(spawns).isEmpty();
        }

        @Test
        @DisplayName("1 spawn: solo primera esquina")
        void oneSpawnShouldReturnFirstCorner() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(1, size);

            assertThat(spawns).hasSize(1);
            assertThat(spawns.get(0)).isEqualTo(new SpawnPoint(-size, -size));
        }

        @Test
        @DisplayName("3 spawns: primeras tres esquinas")
        void threeSpawnsShouldReturnThreeCorners() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(3, size);

            assertThat(spawns).hasSize(3);
            assertThat(spawns).containsExactlyInAnyOrder(
                new SpawnPoint(-size, -size),
                new SpawnPoint(size, -size),
                new SpawnPoint(size, size)
            );
        }

        @Test
        @DisplayName("Tamaño de mapa muy pequeño (100 bloques)")
        void verySmallMapSize() {
            int size = 100;
            List<SpawnPoint> spawns = calculateSpawns(8, size);

            assertThat(spawns).hasSize(8);

            for (SpawnPoint spawn : spawns) {
                boolean onPerimeter = 
                    spawn.x == -size || spawn.x == size || 
                    spawn.z == -size || spawn.z == size;
                assertThat(onPerimeter).isTrue();
            }
        }

        @Test
        @DisplayName("Tamaño de mapa muy grande (10000 bloques)")
        void veryLargeMapSize() {
            int size = 10000;
            List<SpawnPoint> spawns = calculateSpawns(4, size);

            assertThat(spawns).hasSize(4);
            assertThat(spawns).containsExactlyInAnyOrder(
                new SpawnPoint(-size, -size),
                new SpawnPoint(size, -size),
                new SpawnPoint(size, size),
                new SpawnPoint(-size, size)
            );
        }

        @Test
        @DisplayName("Número impar de spawns (13)")
        void oddNumberOfSpawns() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(13, size);

            assertThat(spawns).hasSize(13);

            Set<SpawnPoint> uniqueSpawns = new HashSet<>(spawns);
            assertThat(uniqueSpawns).hasSize(13);
        }

        @Test
        @DisplayName("Número primo de spawns (17)")
        void primeNumberOfSpawns() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(17, size);

            assertThat(spawns).hasSize(17);

            assertThat(spawns.subList(0, 4)).hasSize(4);

            assertThat(spawns.subList(4, 17)).hasSize(13);
        }
    }

    @Nested
    @DisplayName("Escenarios Reales de Juego")
    class RealGameScenariosTests {

        @Test
        @DisplayName("Partida típica de 10 jugadores FFA")
        void typicalTenPlayerFFAGame() {
            int size = 1000;
            List<SpawnPoint> spawns = calculateSpawns(10, size);

            assertThat(spawns).hasSize(10);

            for (SpawnPoint spawn : spawns) {
                boolean onPerimeter = 
                    spawn.x == -size || spawn.x == size || 
                    spawn.z == -size || spawn.z == size;
                assertThat(onPerimeter).isTrue();
            }

            for (int i = 0; i < spawns.size(); i++) {
                for (int j = i + 1; j < spawns.size(); j++) {
                    double dist = spawns.get(i).distanceTo(spawns.get(j));
                    assertThat(dist)
                        .withFailMessage("Spawns %s y %s demasiado cercanos: %f", 
                            spawns.get(i), spawns.get(j), dist)
                        .isGreaterThan(300);
                }
            }
        }

        @Test
        @DisplayName("Partida de equipos 2v2v2v2 (4 equipos)")
        void fourTeamsGame() {
            int size = 1500;
            int teamCount = 4;
            List<SpawnPoint> spawns = calculateSpawns(teamCount, size);

            assertThat(spawns).hasSize(4);

            assertThat(spawns).containsExactlyInAnyOrder(
                new SpawnPoint(-size, -size),
                new SpawnPoint(size, -size),
                new SpawnPoint(size, size),
                new SpawnPoint(-size, size)
            );

            double maxDistance = spawns.get(0).distanceTo(spawns.get(2));
            assertThat(maxDistance).isGreaterThan(4000);
        }

        @Test
        @DisplayName("Partida grande de 24 jugadores")
        void largeTwentyFourPlayerGame() {
            int size = 2000;
            List<SpawnPoint> spawns = calculateSpawns(24, size);

            assertThat(spawns).hasSize(24);

            Set<SpawnPoint> uniqueSpawns = new HashSet<>(spawns);
            assertThat(uniqueSpawns).hasSize(24);

            List<Double> consecutiveDistances = new ArrayList<>();
            for (int i = 0; i < spawns.size() - 1; i++) {
                consecutiveDistances.add(spawns.get(i).distanceTo(spawns.get(i + 1)));
            }

            double avgDistance = consecutiveDistances.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

            assertThat(avgDistance).isBetween(500.0, 1500.0);
        }
    }

    @Nested
    @DisplayName("Validación de Invariantes del Algoritmo")
    class AlgorithmInvariantsTests {

        @Test
        @DisplayName("Invariante: cantidad exacta de spawns generados")
        void exactSpawnCountInvariant() {
            for (int count = 0; count <= 50; count++) {
                List<SpawnPoint> spawns = calculateSpawns(count, 1000);
                assertThat(spawns).hasSize(count);
            }
        }

        @Test
        @DisplayName("Invariante: todos los spawns en el perímetro")
        void allSpawnsOnPerimeterInvariant() {
            int size = 1000;
            for (int count = 1; count <= 30; count++) {
                List<SpawnPoint> spawns = calculateSpawns(count, size);

                for (SpawnPoint spawn : spawns) {
                    boolean onPerimeter = 
                        spawn.x == -size || spawn.x == size || 
                        spawn.z == -size || spawn.z == size;
                    assertThat(onPerimeter)
                        .withFailMessage("Para %d spawns, el spawn %s no está en perímetro", count, spawn)
                        .isTrue();
                }
            }
        }

        @Test
        @DisplayName("Invariante: sin spawns duplicados")
        void noDuplicateSpawnsInvariant() {
            int size = 1000;
            for (int count = 1; count <= 40; count++) {
                List<SpawnPoint> spawns = calculateSpawns(count, size);
                Set<SpawnPoint> uniqueSpawns = new HashSet<>(spawns);
                
                assertThat(uniqueSpawns)
                    .withFailMessage("Para %d spawns, se encontraron duplicados", count)
                    .hasSize(count);
            }
        }

        @Test
        @DisplayName("Invariante: primeros 4 spawns siempre son las esquinas (para n>=4)")
        void firstFourAlwaysCornersInvariant() {
            int size = 1000;
            for (int count = 4; count <= 20; count++) {
                List<SpawnPoint> spawns = calculateSpawns(count, size);

                assertThat(spawns.subList(0, 4))
                    .withFailMessage("Para %d spawns, las primeras 4 no son las esquinas", count)
                    .containsExactlyInAnyOrder(
                        new SpawnPoint(-size, -size),
                        new SpawnPoint(size, -size),
                        new SpawnPoint(size, size),
                        new SpawnPoint(-size, size)
                    );
            }
        }
    }

    @Nested
    @DisplayName("Escenarios Solicitados por el Usuario")
    class UserRequestedScenariosTests {

        @Test
        @DisplayName("Escenario: 10 equipos preformados - Verificación de Uniformidad")
        void tenPreformedTeamsUniformity() {
            int size = 1000;
            int teamCount = 10;
            List<SpawnPoint> spawns = calculateSpawns(teamCount, size);

            assertThat(spawns).hasSize(10);

            // Con el nuevo algoritmo, los 6 spawns extra se distribuyen [2, 2, 1, 1]
            // Lado Oeste (Side 4) ahora tiene 1 spawn extra.
            // La distancia entre esquinas en el lado Oeste ya no es 2000.
            
            SpawnPoint sw = new SpawnPoint(-size, size); // Esquina 4
            SpawnPoint nw = new SpawnPoint(-size, -size); // Esquina 1
            
            // El spawn extra en el lado Oeste está a la mitad (fraction = 1/2)
            SpawnPoint westExtra = new SpawnPoint(-size, 0);
            assertThat(spawns).contains(westExtra);

            double gap1 = sw.distanceTo(westExtra);
            double gap2 = westExtra.distanceTo(nw);
            
            assertThat(gap1).isEqualTo(1000.0);
            assertThat(gap2).isEqualTo(1000.0);
        }

        @Test
        @DisplayName("Escenario: 14 jugadores con equipos in-game (asumiendo 7 equipos de 2)")
        void fourteenPlayersSevenTeams() {
            int size = 1000;
            int teamCount = 7; // 14 players / 2 per team
            List<SpawnPoint> spawns = calculateSpawns(teamCount, size);

            assertThat(spawns).hasSize(7);

            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < spawns.size(); i++) {
                for (int j = i + 1; j < spawns.size(); j++) {
                    minDistance = Math.min(minDistance, spawns.get(i).distanceTo(spawns.get(j)));
                }
            }

            // 8000 / 7 = ~1142 de separación ideal.
            assertThat(minDistance).isGreaterThan(600);
        }

        @Test
        @DisplayName("Escenario: 14 jugadores con equipos in-game (asumiendo 14 equipos de 1)")
        void fourteenPlayersFourteenTeams() {
            int size = 1000;
            int teamCount = 14;
            List<SpawnPoint> spawns = calculateSpawns(teamCount, size);

            assertThat(spawns).hasSize(14);

            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < spawns.size(); i++) {
                for (int j = i + 1; j < spawns.size(); j++) {
                    minDistance = Math.min(minDistance, spawns.get(i).distanceTo(spawns.get(j)));
                }
            }

            // 8000 / 14 = ~571 de separación ideal.
            assertThat(minDistance).isGreaterThan(400);
        }
    }
}
