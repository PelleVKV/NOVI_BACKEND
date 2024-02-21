package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.exceptions.NotFoundException;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.repositories.AirplaneRepository;
import org.hibernate.hql.internal.ast.tree.FkRefNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;

    @Autowired
    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    public List<Airplane> getAllAirplanes() {
        return airplaneRepository.findAll();
    }

    public Airplane getAirplaneByAirplaneCode(String airplaneCode) {
        return airplaneRepository.findByAirplaneCode(airplaneCode).orElse(null);
    }

    public AirportDTO getResidingAirport(String airplaneCode, LocalDateTime departureDate) {
        try {
            Airplane airplane = getAirplaneByAirplaneCode(airplaneCode);

            Set<Flight> flights = airplane.getFlights();
            List<Flight> flightList = new ArrayList<>(flights);
            flightList.sort(Comparator.comparing(Flight::getEtd));

            // Binding new residing airport after flight to flight dates
            LinkedHashMap<Airport, AbstractMap.SimpleEntry<LocalDateTime, LocalDateTime>> flightPeriods = new LinkedHashMap<>();
            for (Flight flight : flightList) {
                flightPeriods.put(flight.getArrivalAirport(), new AbstractMap.SimpleEntry<>(flight.getEtd(), flight.getEta()));
            }

            LocalDateTime rightDate = null;
            Airport currentResidingAirport = null;
            for (HashMap.Entry<Airport, AbstractMap.SimpleEntry<LocalDateTime, LocalDateTime>> flightPeriod : flightPeriods.entrySet()) {
                // Getting new Airport, departure date and arrival date in set
                Airport airport = flightPeriod.getKey();
                LocalDateTime date1 = flightPeriod.getValue().getKey();
                LocalDateTime date2 = flightPeriod.getValue().getValue();

                // If departureDate is between date 1 and 2 then the airplane is not available
                if (departureDate.isAfter(date1) && departureDate.isBefore(date2)) {
                    return null;
                }

                // Checking if this date is before the departureDate, if found select the new residing airport
                if (date2.isBefore(departureDate)) {
                    rightDate = date2;
                    currentResidingAirport = airport;
                }
            }

            if (rightDate != null) {
                return AirportService.fromAirport(currentResidingAirport);
            } else {
                // Return initial residing airport if nothing found
                Airplane airplane1 = getAirplaneByAirplaneCode(airplaneCode);
                return AirportService.fromAirport(airplane1.getResidingAirport());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get current airport");
        }
    }

    public Airplane saveAirplane(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public List<Airplane> saveAirplanes(List<Airplane> airplanes) {
        return airplaneRepository.saveAll(airplanes);
    }

    public void deleteAirplane(String airplaneCode) {
        airplaneRepository.deleteById(airplaneCode);
    }

}
