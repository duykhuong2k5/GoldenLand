package com.example.demo.config;

import com.example.demo.entity.Building;
import com.example.demo.entity.Customer;
import com.example.demo.entity.PriceHistory;
import com.example.demo.entity.RentArea;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.BuildingRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PriceHistoryRepository;
import com.example.demo.repository.RentAreaRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class SampleDataSeeder {

    private static final Logger log = LoggerFactory.getLogger(SampleDataSeeder.class);

    @Bean
    CommandLineRunner seedSampleData(RoleRepository roleRepository,
                                     UserRepository userRepository,
                                     CustomerRepository customerRepository,
                                     BuildingRepository buildingRepository,
                                     RentAreaRepository rentAreaRepository,
                                     PriceHistoryRepository priceHistoryRepository,
                                     PasswordEncoder passwordEncoder,
                                     @Value("${app.seed.enabled:true}") boolean seedEnabled) {
        return args -> {
            if (!seedEnabled) {
                log.info("Sample data seeding is disabled (app.seed.enabled=false)");
                return;
            }

            Role adminRole = upsertRole(roleRepository, "ADMIN", "Quản trị viên");
            Role managerRole = upsertRole(roleRepository, "MANAGER", "Quản lý");
            Role staffRole = upsertRole(roleRepository, "STAFF", "Nhân viên");

            User admin = upsertUser(userRepository, passwordEncoder,
                    "admin", "Admin GoldenLand", "admin@goldenland.local", "0900000001", 1, adminRole);
            User manager = upsertUser(userRepository, passwordEncoder,
                    "manager", "Manager GoldenLand", "manager@goldenland.local", "0900000002", 1, managerRole);
            User staffA = upsertUser(userRepository, passwordEncoder,
                    "staff01", "Staff One", "staff01@goldenland.local", "0900000003", 1, staffRole);
            User staffB = upsertUser(userRepository, passwordEncoder,
                    "staff02", "Staff Two", "staff02@goldenland.local", "0900000004", 1, staffRole);

            Customer customerA = upsertCustomer(customerRepository, passwordEncoder,
                    "customer01", "Khách Hàng A", "customer01@goldenland.local", "0911000001", "Công ty A", "Tìm văn phòng 150m2");
            Customer customerB = upsertCustomer(customerRepository, passwordEncoder,
                    "customer02", "Khách Hàng B", "customer02@goldenland.local", "0911000002", "Công ty B", "Tìm mặt bằng tầng trệt");

            if (buildingRepository.count() == 0) {
                List<Building> samples = new ArrayList<>();
                samples.add(building("Golden Tower", "12 Nguyễn Huệ", "Bến Nghé", "Quận 1", "Cho thuê văn phòng", 1200, 350000, customerA, "APPROVED"));
                samples.add(building("City Light", "88 Điện Biên Phủ", "Đa Kao", "Quận 1", "Cho thuê căn hộ chung cư", 800, 220000, customerA, "APPROVED"));
                samples.add(building("Riverside Hub", "25 Võ Văn Kiệt", "Cầu Ông Lãnh", "Quận 1", "Tầng Trệt", 600, 180000, customerB, "PENDING"));

                samples = buildingRepository.saveAll(samples);

                samples.get(0).setUserEntities(List.of(manager, staffA));
                samples.get(1).setUserEntities(List.of(staffA));
                samples.get(2).setUserEntities(List.of(staffB));
                buildingRepository.saveAll(samples);

                saveRentAreas(rentAreaRepository, samples.get(0), List.of(100, 150, 200));
                saveRentAreas(rentAreaRepository, samples.get(1), List.of(60, 90, 120));
                saveRentAreas(rentAreaRepository, samples.get(2), List.of(40, 70));

                for (Building b : samples) {
                    seedQuarterPriceHistory(priceHistoryRepository, b);
                }

                log.info("Sample data seeded successfully: {} buildings, {} users, {} customers",
                        samples.size(), userRepository.count(), customerRepository.count());
            } else {
                log.info("Sample data seeding skipped because building data already exists");
            }

            // Mật khẩu mặc định cho tất cả tài khoản mẫu là 123456.
        };
    }

    private Role upsertRole(RoleRepository roleRepository, String code, String name) {
        Role role = roleRepository.findAll().stream()
                .filter(r -> code.equalsIgnoreCase(r.getCode()))
                .findFirst()
                .orElse(null);

        if (role == null) {
            role = new Role();
            role.setCode(code);
        }

        role.setName(name);
        return roleRepository.save(role);
    }

    private User upsertUser(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            String username,
                            String fullName,
                            String email,
                            String phone,
                            int status,
                            Role role) {
        User user = userRepository.findOneByUserName(username);
        if (user == null) {
            user = new User();
            user.setUserName(username);
            user.setPassword(passwordEncoder.encode("123456"));
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setStatus(status);
        user.setRoles(List.of(role));
        return userRepository.save(user);
    }

    private Customer upsertCustomer(CustomerRepository customerRepository,
                                    PasswordEncoder passwordEncoder,
                                    String username,
                                    String fullName,
                                    String email,
                                    String phone,
                                    String company,
                                    String demand) {
        Customer customer = customerRepository.findByUsername(username);
        if (customer == null) {
            customer = new Customer();
            customer.setUsername(username);
            customer.setPassword(passwordEncoder.encode("123456"));
        }

        customer.setFullname(fullName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setCompanyname(company);
        customer.setDemand(demand);
        customer.setStatus("Chưa xử lý");
        customer.setIsActive(1);
        return customerRepository.save(customer);
    }

    private Building building(String name,
                              String street,
                              String ward,
                              String district,
                              String typeCode,
                              int floorArea,
                              int rentPrice,
                              Customer customer,
                              String status) {
        Building b = new Building();
        b.setName(name);
        b.setStreet(street);
        b.setWard(ward);
        b.setDistrict(district);
        b.setTypeCode(typeCode);
        b.setFloorArea(floorArea);
        b.setRentPrice(rentPrice);
        b.setServiceFee("Theo thỏa thuận");
        b.setManagerName("GoldenLand Support");
        b.setManagerPhone("0909090909");
        b.setDescription("Dữ liệu mẫu cho môi trường demo.");
        b.setStatus(status);
        b.setCustomer(customer);
        b.setViewCount(0L);
        return b;
    }

    private void saveRentAreas(RentAreaRepository rentAreaRepository, Building building, List<Integer> areas) {
        List<RentArea> rentAreas = new ArrayList<>();
        for (Integer area : areas) {
            RentArea ra = new RentArea();
            ra.setBuilding(building);
            ra.setValue(area);
            rentAreas.add(ra);
        }
        rentAreaRepository.saveAll(rentAreas);
    }

    private void seedQuarterPriceHistory(PriceHistoryRepository priceHistoryRepository, Building building) {
        Set<String> existingPeriods = new HashSet<>();
        for (PriceHistory item : priceHistoryRepository.findByBuildingOrderByPeriod(building.getId())) {
            existingPeriods.add(item.getPeriod());
        }

        LocalDate now = LocalDate.now();
        BigDecimal base = BigDecimal.valueOf(Math.max(30, building.getRentPrice() / 10000.0));

        for (int i = 3; i >= 0; i--) {
            LocalDate d = now.minusMonths((long) i * 3);
            int quarter = ((d.getMonthValue() - 1) / 3) + 1;
            String period = d.getYear() + "Q" + quarter;

            if (existingPeriods.contains(period)) {
                continue;
            }

            PriceHistory p = new PriceHistory();
            p.setBuilding(building);
            p.setPeriod(period);

            BigDecimal med = base.add(BigDecimal.valueOf((3 - i) * 0.8));
            p.setPriceMed(med.setScale(1, RoundingMode.HALF_UP));
            p.setPriceMin(med.multiply(BigDecimal.valueOf(0.95)).setScale(1, RoundingMode.HALF_UP));
            p.setPriceMax(med.multiply(BigDecimal.valueOf(1.08)).setScale(1, RoundingMode.HALF_UP));
            p.setListings(5 + i);

            priceHistoryRepository.save(p);
        }
    }
}
