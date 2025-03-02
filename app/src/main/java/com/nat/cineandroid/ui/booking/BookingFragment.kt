package com.nat.cineandroid.ui.booking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nat.cineandroid.data.session.entity.SeatEntity
import com.nat.cineandroid.databinding.FragmentBookingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingFragment : Fragment() {
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookingViewModel by viewModels()
    private val args: BookingFragmentArgs by navArgs()

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

        timeAdapter = TimeAdapter { time ->
            Log.d("SelectSessionFragment", "Time clicked: $time")
            viewModel.onTimeSelected(time)
            timeAdapter.setSelectedTime(time)
        }

        dateAdapter = DateAdapter { date ->
            Log.d("SelectSessionFragment", "Date clicked: $date")
            viewModel.onDateSelected(date)
            dateAdapter.setSelectedDate(date)
        }

        seatAdapter = SeatAdapter { seat ->
            Log.d("SelectSessionFragment", "Seat clicked: ${seat.seatNumber}")
        }

        binding.checkoutButton.setOnClickListener {
            val selectedSeats = seatAdapter.getSelectedSeats()

            if (selectedSeats.isNotEmpty()) {
                val action = BookingFragmentDirections.actionBookingFragmentToPaymentTab(
                    movieId = args.movieId,
                    theaterId = args.theaterId,
                    sessionId = viewModel.selectedSession.value!!.session.id,
                    seatIds = selectedSeats.map { it.seatNumber }.toIntArray()
                )
                findNavController().navigate(action)
            }
        }

        binding.backButton.root.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.timeRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManager = GridLayoutManager(requireContext(), 22) // 8 colonnes
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = seatAdapter.getItemAt(position)
                return when (item) {
                    is SeatItem.SeatData -> 2
                    is SeatItem.Gap -> 1
                }
            }
        }
        binding.seatRecyclerView.layoutManager = layoutManager


        binding.timeRecyclerView.adapter = timeAdapter
        binding.dateRecyclerView.adapter = dateAdapter
        binding.seatRecyclerView.adapter = seatAdapter

        viewModel.selectedSession.observe(viewLifecycleOwner) { sessionWithSeats ->
            sessionWithSeats?.let {
                val seatItems = generateSeatItems(it.seats)
                seatAdapter.submitList(seatItems)
                seatAdapter.resetSelection()
            }
        }

        viewModel.sessions.observe(viewLifecycleOwner) { sessions ->
            Log.d("SelectSessionFragment", "Sessions: $sessions")
            if (sessions.isNotEmpty()) {
                val dates = sessions.map { viewModel.extractDate(it.session.startTime) }
                    .distinct()
                    .sorted()
                sessions.map { viewModel.extractTime(it.session.startTime) }
                    .distinct()
                    .sorted()

                dateAdapter.submitList(dates)
            }
        }

        viewModel.availableTimes.observe(viewLifecycleOwner) { times ->
            timeAdapter.submitList(times)
        }

        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            dateAdapter.setSelectedDate(date)
        }

        viewModel.selectedTime.observe(viewLifecycleOwner) { time ->
            timeAdapter.setSelectedTime(time)
        }

        viewModel.loadSessions(movieId = args.movieId, theaterId = args.theaterId)
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