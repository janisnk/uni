
def sudoku():
    print("Your input")
    rows = []
    for i in range(9):
        row = input()
        if (check_row(row)):
            rows.append(row)
        else:
            raise ValueError('NEMJOO')
    if column_check(rows) and square_check(rows):
        print("YES")
    else:
        print("NO")


def check_row(x):
    container = []
    try:
        counter = 0
        for i in str(x):
            int(i)
            if i in container:
                return False
            else:
                container.append(i)
            counter += 1
        return counter == 9
    except:
        print("Input row must contain 9 digits")


def column_check(rows):
    for i in range(9):
        cols = []
        for num in rows:
            n = int(str(num)[i])
            if n in cols:
                return False
            else :
                cols.append(n)
        cols = []
    return True


def square_check(rows):
    for i in range(0,9,3):
        for j in range(0,9,3):
            s = set()
            fs,sc,td = rows[i],rows[i+1], rows[i+2]
            concat = str(fs)[:j+3] + str(sc)[:j+3] + str(td)[:j+3]
            [elem for elem in concat if not (elem in s or s.add(elem))]
            if len(s) != 9:
                return False
    return True

sudoku()
