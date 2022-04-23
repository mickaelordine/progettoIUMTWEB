import time
from tkinter import Tk, Label, StringVar, Button, Entry, ttk
from tkinter import *

import numpy as np

window = Tk()
window.title("Matrix")
window.geometry("900x750+120+120")
window.configure(bg='bisque2')
window.resizable(False, False)

# empty arrays for your Entrys and StringVars
text_var = []
entries = []
text_var_prof = []
entries_prof = []
matrix_dimension_row = []
matrix_dimension_col = []
entries_matrix_dimension_row = []
entries_matrix_dimension_col = []

print("inserisci i valori X e Y della matrice grande")
rows, cols = (int(input()), int(input()))  # CxF
print("inserisci i valori X e Y della matrice pattern")
rows_patter, cols_pattern = (int(input()), int(input())) #MxN



generic_flag = rows_patter*cols_pattern
generic_tot_time = (rows-rows_patter + 1)*(cols - cols_pattern + 1) #(C-M+1)*(F-N+1)
generic_number_of_time = (cols - cols_pattern + 1) #numero di volte in cui ci sta il pattern nella stessa row




# callback function to get your StringVars
# normal pattern
def get_pattern():
    matrix = []
    for i in range(rows_patter):
        matrix.append([])
        for j in range(cols_pattern):
            matrix[i].append(text_var[i][j].get())
    return matrix


# range(start, stop, step)
# 90 degree pattern to the left side
def get_pattern_90():
    matrix = get_pattern()
    new_matrix = [[matrix[j][i] for j in range(rows_patter)] for i in range(len(matrix[0]) - 1, -1, -1)]  # the first parameter is the len of cols, the second parameter is how much matrice we have, 5-1 cause is an array, the third is how much matrix we take in attention for our loop
    print(new_matrix)
    return new_matrix


# 180 degree pattern, we have to start from the end
def get_pattern_180():
    matrix = get_pattern()
    new_matrix = [[matrix[i][j] for j in range(cols_pattern - 1, -1, -1)] for i in range(len(matrix[0]) - 1, -1, -1)]
    print(new_matrix)
    return new_matrix


# 270 degree pattern 1 90 degre to the right side
def get_pattern_270():
    matrix = get_pattern()
    #I do 3 times the 90 degree pattern
    new_matrix = [[matrix[j][i] for j in range(rows_patter)] for i in range(len(matrix[0]) - 1, -1, -1)]
    new_new_matrix = [[new_matrix[j][i] for j in range(rows_patter)] for i in range(len(new_matrix[0]) - 1, -1, -1)]
    final_matrix = [[new_new_matrix[j][i] for j in range(rows_patter)] for i in range(len(new_new_matrix[0]) - 1, -1, -1)]
    print(final_matrix)
    return final_matrix


def get_prof():
    matrix = []
    for i in range(rows):
        matrix.append([])
        for j in range(cols):
            matrix[i].append(text_var_prof[i][j].get())
    return matrix


#il totale delle volte in cui il pattern può stare all'interno è (len(matrix_prof) - 1) * (len(matrix_prof) - 1)

# OGNI VOLTA CHE FINIAMO UN BLOCCO LO SPOSTIAMO VERSO DX DI 1, APPENA RAGGIUNGE IL
# VALORE DI VOLTE CHE LO DEVE FARE LO SETTIAMO A 0 E AGGIUNGIAMO 1 ALLA RIGA SUCCESSIVA
# DA LEGGERE


def get_0_result(matrix, matrix_prof):
    tot_times = generic_tot_time
    flag = 0
    n_pattern = 0
    new_start_col = 0
    new_start_row = 0
    number_of_time = generic_number_of_time
    for tot in range(tot_times):
        for i in range(rows_patter):
            for j in range(cols_pattern):
                if matrix[i][j] != matrix_prof[i + new_start_row][j + new_start_col]:
                    print("")
                else:
                    flag += 1
        if flag == generic_flag:
            n_pattern += 1
            number_of_time -= 1
        else:
            number_of_time -= 1
        flag = 0
        new_start_col += 1
        if number_of_time == 0:
            new_start_row += 1
            new_start_col = 0
            number_of_time = generic_number_of_time
    print(n_pattern)
    return n_pattern


def get_90_result(matrix, matrix_prof):
    tot_times = generic_tot_time
    flag = 0
    n_pattern = 0
    new_start_col = 0
    new_start_row = 0
    number_of_time = generic_number_of_time
    for tot in range(tot_times):
        for i in range(rows_patter):
            for j in range(cols_pattern):
                if matrix[i][j] != matrix_prof[i + new_start_row][j + new_start_col]:
                    print("")
                else:
                    flag += 1
        if flag == generic_flag:
            n_pattern += 1
            number_of_time -= 1
        else:
            number_of_time -= 1
        flag = 0
        new_start_col += 1
        if number_of_time == 0:
            new_start_row += 1
            new_start_col = 0
            number_of_time = generic_number_of_time
    return n_pattern


def get_180_result(matrix, matrix_prof):
    tot_times = generic_tot_time
    flag = 0
    n_pattern = 0
    new_start_col = 0
    new_start_row = 0
    number_of_time = generic_number_of_time
    for tot in range(tot_times):
        for i in range(rows_patter):
            for j in range(cols_pattern):
                if matrix[i][j] != matrix_prof[i + new_start_row][j + new_start_col]:
                    print("")
                else:
                    flag += 1
        if flag == generic_flag:
            n_pattern += 1
            number_of_time -= 1
        else:
            number_of_time -= 1
        flag = 0
        new_start_col += 1
        if number_of_time == 0:
            new_start_row += 1
            new_start_col = 0
            number_of_time = generic_number_of_time
    return n_pattern


def get_270_result(matrix, matrix_prof):
    tot_times = generic_tot_time
    flag = 0
    n_pattern = 0
    new_start_col = 0
    new_start_row = 0
    number_of_time = generic_number_of_time
    for tot in range(tot_times):
        for i in range(rows_patter):
            for j in range(cols_pattern):
                if matrix[i][j] != matrix_prof[i + new_start_row][j + new_start_col]:
                    print("")
                else:
                    flag += 1
        if flag == generic_flag:
            n_pattern += 1
            number_of_time -= 1
        else:
            number_of_time -= 1
        flag = 0
        new_start_col += 1
        if number_of_time == 0:
            new_start_row += 1
            new_start_col = 0
            number_of_time = generic_number_of_time
    return n_pattern


def get_result():
    matrix = get_pattern()
    matrix_90 = get_pattern_90()
    matrix_180 = get_pattern_180()
    matrix_270 = get_pattern_270()
    matrix_prof = get_prof()

    # THIS IS THE CHECK OF HOW MUCH PATTERN WE HAVE FOUND

    n_patter_0 = get_0_result(matrix, matrix_prof)

    n_patter_90 = get_90_result(matrix_90, matrix_prof)

    n_patter_180 = get_180_result(matrix_180, matrix_prof)

    n_patter_270 = get_270_result(matrix_270, matrix_prof)

    open_popup_find(n_patter_0, n_patter_90, n_patter_180, n_patter_270, matrix, matrix_90, matrix_180, matrix_270, 0,
                    90, 180, 270)



def set_all_zero():
    matrix = []
    for i in range(rows_patter):
        matrix.append([])
        for j in range(cols_pattern):
            matrix[i].append(text_var[i][j].set(0))


def set_all_zero_prof():
    matrix = []
    for i in range(rows):
        matrix.append([])
        for j in range(cols):
            matrix[i].append(text_var_prof[i][j].set(0))


# set the first matrix
Label(window, text="Enter the pattern :", font=('arial', 10, 'bold'), bg="bisque2").place(x=20, y=20)



x2 = 0
y2 = 0

for i in range(rows_patter):
    # append an empty list to your two arrays
    # so you can append to those later
    text_var.append([])
    entries.append([])
    for j in range(cols_pattern):
        # append your StringVar and Entry
        text_var[i].append(StringVar())
        entries[i].append(Entry(window, textvariable=text_var[i][j], width=3))  # here we set the dimension on squares
        entries[i][j].place(x=60 + x2, y=50 + y2)  # create the correct place for the entire matrix
        x2 += 30

    # create a distance between input square
    y2 += 30
    x2 = 0

#def set_label_for_matrix:
# set the second matrix
Label(window, text="Write what you want :", font=('arial', 10, 'bold'), bg="bisque2").place(x=420, y=20)

x3 = 0
y3 = 0
for i in range(rows):
    # append an empty list to your two arrays
    # so you can append to those later
    text_var_prof.append([])
    entries_prof.append([])
    for j in range(cols):
        # append your StringVar and Entry
        text_var_prof[i].append(StringVar())
        entries_prof[i].append(
            Entry(window, textvariable=text_var_prof[i][j], width=3))  # here we set the dimension on squares
        entries_prof[i][j].place(x=460 + x3, y=50 + y3)  # create the correct place for the entire matrix
        x3 += 30

    # create a distance between input square
    y3 += 30
    x3 = 0


def set_value_of_matrix():
    top = Toplevel(window)
    top.geometry("600x500+400+400")
    top.title("Child Window")
    top.configure(bg='bisque2')
    matrix_dimension_row.append(StringVar())
    matrix_dimension_col.append(StringVar())
    entries_matrix_dimension_row.append(Entry(window,textvaiable=matrix_dimension_row))
    entries_matrix_dimension_row.place(x=200,y=30)
    entries_matrix_dimension_col.append(Entry(window, textvaiable=matrix_dimension_col))
    entries_matrix_dimension_col.place(x=200, y=60)

# menu popup when we found the pattern
def open_popup_find(n_pattern_0, n_pattern_90, n_pattern_180, n_pattern_270, m_0, m_90, m_180, m_270, degree_0,
                    degree_90, degree_180, degree_270, ):
    # Call numpy.array(object) to get a NumPy array containing each row and column of the two-dimensional list object.
    list_as_array_0 = np.array(m_0)
    list_as_array_90 = np.array(m_90)
    list_as_array_180 = np.array(m_180)
    list_as_array_270 = np.array(m_270)
    top = Toplevel(window)
    top.geometry("600x500+400+400")
    top.title("Child Window")
    top.configure(bg='bisque2')

    # 0 degree pattern print
    Label(top, text="pattern found " + str(n_pattern_0) + " times in " + str(degree_0) + " degrees!!!", bg='bisque2',
          font=('arial', 10, 'bold')).place(x=20, y=40)
    Label(top, text=list_as_array_0, bg='bisque2', font=('arial', 12, 'bold')).place(x=40, y=70)
    # 90 degree pattern print
    Label(top, text="pattern found " + str(n_pattern_90) + " times in " + str(degree_90) + " degrees!!!", bg='bisque2',
          font=('arial', 10, 'bold')).place(x=320, y=40)
    Label(top, text=list_as_array_90, bg='bisque2', font=('arial', 12, 'bold')).place(x=340, y=70)
    # 180 degree pattern print
    Label(top, text="pattern found " + str(n_pattern_180) + " times in " + str(degree_180) + " degrees!!!",
          bg='bisque2',
          font=('arial', 10, 'bold')).place(x=20, y=240)
    Label(top, text=list_as_array_180, bg='bisque2', font=('arial', 12, 'bold')).place(x=40, y=270)
    # 270 degree pattern print
    Label(top, text="pattern found " + str(n_pattern_270) + " times in " + str(degree_270) + " degrees!!!",
          bg='bisque2',
          font=('arial', 10, 'bold')).place(x=320, y=240)
    Label(top, text=list_as_array_270, bg='bisque2', font=('arial', 12, 'bold')).place(x=340, y=270)


# menu popup when we don't found the pattern
def open_popup_not_find():
    top = Toplevel(window)
    top.geometry("300x150+400+400")
    top.title("Child Window")
    top.configure(bg='#C24641')
    Label(top, text="sorry... Pattern not found!", bg='#C24641', font=('arial', 10, 'bold')).place(x=20, y=40)


# buttons for pattern
zeroButton = Button(window, text="refresh", bg='bisque3', width=15, command=set_all_zero)
zeroButton.place(x=50, y=240)

# buttons for prof
button_prof = Button(window, text="Submit", bg='bisque3', width=15, command=get_result)
button_prof.place(x=460, y=200)
zeroButton_prof = Button(window, text="refresh", bg='bisque3', width=15, command=set_all_zero_prof)
zeroButton_prof.place(x=460, y=240)

window.mainloop()

# CREARE LO STORICO dei pattern, scriverli subito dopo che sono stati trovati con gli 0 neri e gli 1 rossi

##RICORDARSI DI METTERE LE DIMENSIONI DEI PATTERN E DELLA MATRICE E CAMBIUARE IL VALORE DI FLAG IN BASE A QUANTI VALE IL PATTERN