package com.pratik.boxspinner

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.pratik.boxspinner.databinding.BoxSpinnerViewBinding

class BoxSpinner @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val TAG = "BoxSpinner"
    private val binding =
        BoxSpinnerViewBinding.inflate(LayoutInflater.from(context), this, true)

    private var arrayAdapter: ArrayAdapter<Any>? = null
    private var itemSelectedListener: OnItemSelectedListener? = null
    private var list: List<Any>? = null
    private var displayList: List<Any>? = null

    private var mItemSelected: Any? = null
    private var mItemSelectedPosition: Int? = null
    private var mItemSelectedId: Long? = null
    private var mHint: String? = null

    private var _error: String? = null

    var error: String?
        get() = _error
        set(value) {
            binding.textInputLayout.error = value
        }

    var selectedItem: Any
        get() = displayList?.get(0)!!
        set(value) {
            binding.autoCompleteTextView.setText(displayList!![value as Int].toString())
        }

    var hint: String?
        get() = mHint
        set(value) {
            binding.textInputLayout.hint = value
        }

    var isErrorEnabled: Boolean
        get() = binding.textInputLayout.isErrorEnabled
        set(value) {
            binding.textInputLayout.isErrorEnabled = value
        }

    init {
        val attributes = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.BoxSpinner,
            defStyleAttr,
            defStyleAttr
        )
        val hint = attributes.getString(R.styleable.BoxSpinner_hint)
        val spinnerIcon = attributes.getResourceId(R.styleable.BoxSpinner_icon, 0)
        val iconPosition = attributes.getInt(R.styleable.BoxSpinner_iconPosition, 0)
        val searchable = attributes.getBoolean(R.styleable.BoxSpinner_isSearchable, false)

        setSpinnerIcon(spinnerIcon, iconPosition)
        this.hint = hint
        isSearchable(searchable)

        binding.autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Log.d(TAG, "onItemClick: executed1")
                mItemSelected = parent!!.getItemAtPosition(position)
                mItemSelectedPosition = position
                mItemSelectedId = id
                itemSelectedListener?.onItemSelected(list!![position])
            }
    }

    fun setItems(list: List<Any>, displayList: List<String>) {
        this.list = list
        this.displayList = displayList
        this.arrayAdapter = ArrayAdapter(context, R.layout.spinner_item, displayList)
        binding.autoCompleteTextView.setAdapter(this.arrayAdapter)
    }

    fun setSpinnerIcon(icon: Int, position: Int = Position.START.ordinal) {
        when (position) {
            0 -> binding.textInputLayout.setStartIconDrawable(icon)
            1 -> binding.textInputLayout.setEndIconDrawable(icon)
        }
    }

    fun setOnItemSelectedListener(itemSelectedListener: OnItemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener
    }

    fun getOnItemSelectedListener(): OnItemSelectedListener? {
        return this.itemSelectedListener
    }

    fun getSelectedItemPosition(): Int? {
        return mItemSelectedPosition
    }

    fun isSearchable(searchable: Boolean) {
        when (searchable) {
            true -> binding.autoCompleteTextView.inputType = EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE
            else -> {

            }
        }
    }
}