package com.nat.cineandroid.ui.booking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nat.cineandroid.data.session.entity.SeatEntity
import com.nat.cineandroid.databinding.FragmentBookingBinding


class BookingFragment : Fragment() {
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private lateinit var timeAdapter: TimeAdapter
    private lateinit var dateAdapter: DateAdapter
    private lateinit var seatAdapter: SeatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation des adapters avec des callbacks qui loggent et mettent à jour l'état de sélection.
        timeAdapter = TimeAdapter { time ->
            Log.d("SelectSessionFragment", "Time clicked: $time")
            timeAdapter.setSelectedTime(time)
        }

        dateAdapter = DateAdapter { date ->
            Log.d("SelectSessionFragment", "Date clicked: $date")
            dateAdapter.setSelectedDate(date)
        }

        seatAdapter = SeatAdapter { seat ->
            Log.d("SelectSessionFragment", "Seat clicked: ${seat.seatNumber}")
        }

        binding.checkoutButton.setOnClickListener {
            val selectedSeats = seatAdapter.getSelectedSeats()
            Log.d("SelectSessionFragment", "Selected seats: $selectedSeats")
        }

        binding.backButton.root.setOnClickListener {
            val action = BookingFragmentDirections.actionBookingFragmentToMovieDetailFragment()
            findNavController().navigate(action)
        }

        // Configuration des RecyclerViews avec leurs LayoutManagers respectifs.
        binding.timeRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManager = GridLayoutManager(requireContext(), 22) // 8 colonnes
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = seatAdapter.getItemAt(position)
                return when (item) {
                    is SeatItem.SeatData -> 2  // Les sièges prennent 2 spans
                    is SeatItem.Gap -> 1       // Les gaps prennent 1 span
                }
            }
        }
        binding.seatRecyclerView.layoutManager = layoutManager


        // Association des adapters aux RecyclerViews.
        binding.timeRecyclerView.adapter = timeAdapter
        binding.dateRecyclerView.adapter = dateAdapter
        binding.seatRecyclerView.adapter = seatAdapter

        // Données en dur pour tester le visuel.
        val dummyTimes = listOf("6:00 pm", "8:45 pm", "10:00 pm")
        val dummyDates = listOf("May 1", "May 2", "May 3", "May 4", "May 5")
        // Création d'une liste de 32 sièges. Pour l'exemple, on marque les sièges dont le numéro est divisible par 5 comme réservés.
        val dummySeats = (1..50).map { seatNumber ->
            // Marquer les sièges dont le numéro est divisible par 5 comme réservés (exemple)
            SeatEntity(
                seatNumber = seatNumber,
                sessionId = 1,
                isReserved = (seatNumber % 5 == 0)
            )
        }
        // Générer la liste de SeatItem avec les gaps
        val seatItems = generateSeatItems(dummySeats)
        seatAdapter.submitList(seatItems)

        // Remplissage des adapters avec les données en dur.
        timeAdapter.submitList(dummyTimes)
        dateAdapter.submitList(dummyDates)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun generateSeatItems(seatList: List<SeatEntity>): List<SeatItem> {
        val seatItems = mutableListOf<SeatItem>()
        var seatIndex = 0
        repeat(5) { // 5 rangées
            // Ajout de 2 sièges
            repeat(2) {
                if (seatIndex < seatList.size) {
                    seatItems.add(SeatItem.SeatData(seatList[seatIndex]))
                    seatIndex++
                }
            }
            // Ajout d'un gap
            seatItems.add(SeatItem.Gap)
            // Ajout de 4 sièges
            repeat(6) {
                if (seatIndex < seatList.size) {
                    seatItems.add(SeatItem.SeatData(seatList[seatIndex]))
                    seatIndex++
                }
            }
            // Ajout d'un gap
            seatItems.add(SeatItem.Gap)
            // Ajout de 2 sièges
            repeat(2) {
                if (seatIndex < seatList.size) {
                    seatItems.add(SeatItem.SeatData(seatList[seatIndex]))
                    seatIndex++
                }
            }
        }
        return seatItems
    }

}